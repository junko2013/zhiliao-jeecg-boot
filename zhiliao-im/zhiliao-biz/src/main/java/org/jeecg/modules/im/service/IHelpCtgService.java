package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.im.entity.HelpCtg;

import java.util.List;

/**
 * @Description: 使用帮助分类
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
public interface IHelpCtgService extends IService<HelpCtg> {

	/**根节点父ID的值*/
	public static final Integer ROOT_PID_VALUE = 0;
	
	/**树节点有子节点状态值*/
	public static final Integer HASCHILD = 1;
	
	/**树节点无子节点状态值*/
	public static final Integer NOCHILD = 0;

	/**
	 * 新增节点
	 *
	 * @param helpCtg
	 */
	void addHelpCtg(HelpCtg helpCtg);
	
	/**
   * 修改节点
   *
   * @param helpCtg
   * @throws JeecgBootException
   */
	void updateHelpCtg(HelpCtg helpCtg) throws JeecgBootException;
	
	/**
	 * 删除节点
	 *
	 * @param id
   * @throws JeecgBootException
	 */
	void deleteHelpCtg(Integer id) throws JeecgBootException;

	  /**
	   * 查询所有数据，无分页
	   *
	   * @param queryWrapper
	   * @return List<HelpCtg>
	   */
    List<HelpCtg> queryTreeListNoPage(QueryWrapper<HelpCtg> queryWrapper);

	/**
	 * 【vue3专用】根据父级编码加载分类字典的数据
	 *
	 * @param parentCode
	 * @return
	 */
	List<SelectTreeModel> queryListByCode(String parentCode);

	/**
	 * 【vue3专用】根据pid查询子节点集合
	 *
	 * @param pid
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(Integer pid);

}
