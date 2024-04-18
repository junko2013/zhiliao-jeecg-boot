package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.entity.HelpCtg;
import org.jeecg.modules.im.mapper.HelpCtgMapper;
import org.jeecg.modules.im.service.IHelpCtgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 使用帮助分类
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Service
public class HelpCtgServiceImpl extends ServiceImpl<HelpCtgMapper, HelpCtg> implements IHelpCtgService {

	@Override
	public void addHelpCtg(HelpCtg helpCtg) {
	   //新增时设置hasChild为0
	    helpCtg.setHasChild(IHelpCtgService.NOCHILD);
		if(oConvertUtils.isEmpty(helpCtg.getParentId())){
			helpCtg.setParentId(IHelpCtgService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			HelpCtg parent = baseMapper.selectById(helpCtg.getParentId());
			if(parent!=null && 1!=parent.getHasChild()){
				parent.setHasChild(1);
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(helpCtg);
	}
	
	@Override
	public void updateHelpCtg(HelpCtg helpCtg) {
		HelpCtg entity = this.getById(helpCtg.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		Integer old_pid = entity.getParentId();
        Integer new_pid = helpCtg.getParentId();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				helpCtg.setParentId(IHelpCtgService.ROOT_PID_VALUE);
			}
			if(!IHelpCtgService.ROOT_PID_VALUE.equals(helpCtg.getParentId())) {
				baseMapper.updateTreeNodeStatus(helpCtg.getParentId(), IHelpCtgService.HASCHILD);
			}
		}
		baseMapper.updateById(helpCtg);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteHelpCtg(Integer id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        String ids = this.queryTreeChildIds(id.toString());
        if(ids.indexOf(",")>0) {
            StringBuilder sb = new StringBuilder();
            String[] idArr = ids.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    HelpCtg helpCtg = this.getById(idVal);
                    Integer pidVal = helpCtg.getParentId();
                    //查询此节点上一级是否还有其他子节点
                    List<HelpCtg> dataList = baseMapper.selectList(new QueryWrapper<HelpCtg>().eq("parent_id", pidVal).notIn("id",Arrays.asList(idArr)));
                    boolean flag = (dataList == null || dataList.isEmpty()) && !Arrays.asList(idArr).contains(pidVal.toString()) && !sb.toString().contains(pidVal.toString());
                    if(flag){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(Integer.valueOf(pid));
            }
        }else{
            HelpCtg helpCtg = this.getById(id);
            if(helpCtg==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(helpCtg.getParentId());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<HelpCtg> queryTreeListNoPage(QueryWrapper<HelpCtg> queryWrapper) {
        List<HelpCtg> dataList = baseMapper.selectList(queryWrapper);
        List<HelpCtg> mapList = new ArrayList<>();
        for(HelpCtg data : dataList){
            Integer pidVal = data.getParentId();
            //递归查询子节点的根节点
            if(pidVal != null && !IHelpCtgService.NOCHILD.equals(pidVal)){
                HelpCtg rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    @Override
    public List<SelectTreeModel> queryListByCode(String parentCode) {
        Integer pid = ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(parentCode)) {
            LambdaQueryWrapper<HelpCtg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(HelpCtg::getParentId, parentCode);
            List<HelpCtg> list = baseMapper.selectList(queryWrapper);
            if (list == null || list.isEmpty()) {
                throw new JeecgBootException("该编码【" + parentCode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new JeecgBootException("该编码【" + parentCode + "】存在多个，请核实!");
            }
            pid = Integer.valueOf(list.get(0).getId());
        }
        return baseMapper.queryListByPid(pid, null);
    }

    @Override
    public List<SelectTreeModel> queryListByPid(Integer pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(Integer pid) {
		if(!IHelpCtgService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<HelpCtg>().eq("parent_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IHelpCtgService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private HelpCtg getTreeRoot(Integer pidVal){
        HelpCtg data =  baseMapper.selectById(pidVal);
        if(data != null && !IHelpCtgService.ROOT_PID_VALUE.equals(data.getParentId())){
            return this.getTreeRoot(data.getParentId());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(!sb.toString().isEmpty()){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<HelpCtg> dataList = baseMapper.selectList(new QueryWrapper<HelpCtg>().eq("parent_id", pidVal));
        if(dataList != null && !dataList.isEmpty()){
            for(HelpCtg tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
