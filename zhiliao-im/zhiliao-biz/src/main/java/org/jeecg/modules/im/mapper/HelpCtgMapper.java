package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.im.entity.HelpCtg;

import java.util.List;
import java.util.Map;

/**
 * @Description: 使用帮助分类
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
public interface HelpCtgMapper extends BaseMapper<HelpCtg> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") Integer id,@Param("status") Integer status);

	/**
	 * 【vue3专用】根据父级ID查询树节点数据
	 *
	 * @param pid
	 * @param query
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(@Param("pid") Integer pid, @Param("query") Map<String, String> query);

}
