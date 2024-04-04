package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserTenantMapper;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <P>
 * 用户部门表实现类
 * <p/>
 * @Author ZhiLin
 *@since 2019-02-22
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {
	@Autowired
	private ISysDepartService sysDepartService;
	@Lazy
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
    private SysUserTenantMapper userTenantMapper;
	

	/**
	 * 根据用户id查询部门信息
	 */
	@Override
	public List<DepartIdModel> queryDepartIdsOfUser(String userId) {
		LambdaQueryWrapper<SysUserDepart> queryUserDep = new LambdaQueryWrapper<SysUserDepart>();
		LambdaQueryWrapper<SysDepart> queryDep = new LambdaQueryWrapper<SysDepart>();
		try {
            queryUserDep.eq(SysUserDepart::getUserId, userId);
			List<String> depIdList = new ArrayList<>();
			List<DepartIdModel> depIdModelList = new ArrayList<>();
			List<SysUserDepart> userDepList = this.list(queryUserDep);
			if(userDepList != null && userDepList.size() > 0) {
			for(SysUserDepart userDepart : userDepList) {
					depIdList.add(userDepart.getDepId());
				}

			if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
				Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
				queryDep.eq(SysDepart::getTenantId,tenantId);
			}
			
			queryDep.in(SysDepart::getId, depIdList);
			List<SysDepart> depList = sysDepartService.list(queryDep);
			//jeecg-boot/issues/3906
			if(depList != null && depList.size() > 0) {
				for(SysDepart depart : depList) {
					depIdModelList.add(new DepartIdModel().convertByUserDepart(depart));
				}
			}
			return depIdModelList;
			}
		}catch(Exception e) {
			e.fillInStackTrace();
		}
		return null;
		
		
	}


	/**
	 * 根据部门id查询用户信息
	 */
	@Override
	public List<SysUser> queryUserByDepId(String depId) {
		LambdaQueryWrapper<SysUserDepart> queryUserDep = new LambdaQueryWrapper<SysUserDepart>();
		queryUserDep.eq(SysUserDepart::getDepId, depId);
		List<String> userIdList = new ArrayList<>();
		List<SysUserDepart> uDepList = this.list(queryUserDep);
		if(uDepList != null && uDepList.size() > 0) {
			for(SysUserDepart uDep : uDepList) {
				userIdList.add(uDep.getUserId());
			}
			List<SysUser> userList = (List<SysUser>) sysUserMapper.selectBatchIds(userIdList);
			for (SysUser sysUser : userList) {
				sysUser.setSalt("");
				sysUser.setPassword("");
			}
			return userList;
		}
		return new ArrayList<SysUser>();
	}

	/**
	 * 根据部门code，查询当前部门和下级部门的 用户信息
	 */
	@Override
	public List<SysUser> queryUserByDepCode(String depCode,String realname) {
		if(oConvertUtils.isNotEmpty(realname)){
			realname = realname.trim();
		}
		List<SysUser> userList = this.baseMapper.queryDepartUserList(depCode, realname);
		Map<String, SysUser> map = new HashMap(5);
		for (SysUser sysUser : userList) {
			// 返回的用户数据去掉密码信息
			sysUser.setSalt("");
			sysUser.setPassword("");
			map.put(sysUser.getId(), sysUser);
		}
		return new ArrayList<SysUser>(map.values());

	}

	/**
	 *
	 * @param departId
	 * @param username
	 * @param realname
	 * @param pageSize
	 * @param pageNo
	 * @param id
	 * @param isMultiTranslate 是否多字段翻译
	 * @return
	 */
	@Override
	public IPage<SysUser> queryDepartUserPageList(String departId, String username, String realname, int pageSize, int pageNo,String id,String isMultiTranslate) {
		IPage<SysUser> pageList = null;
		// 部门ID不存在 直接查询用户表即可
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		if(oConvertUtils.isEmpty(departId)){
			LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
            query.eq(SysUser::getStatus,CommonConstant.STATUS_1);
			if(oConvertUtils.isNotEmpty(username)){
				String COMMA = ",";
				if(oConvertUtils.isNotEmpty(isMultiTranslate) && username.contains(COMMA)){
					String[] usernameArr = username.split(COMMA);
					query.in(SysUser::getUsername,usernameArr);
				}else {
					query.like(SysUser::getUsername, username);
				}
			}
            if(oConvertUtils.isNotEmpty(id)){
                query.eq(SysUser::getId, id);
            }
            query.ne(SysUser::getUsername,"_reserve_user_external");
			//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
			if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
				String tenantId = oConvertUtils.getString(TenantContext.getTenant(), "0");
				List<String> userIdList = userTenantMapper.getUserIdsByTenantId(Integer.valueOf(tenantId));
				if(null!=userIdList && userIdList.size()>0){
                    query.in(SysUser::getId,userIdList);
                }
			}
			pageList = sysUserMapper.selectPage(page, query);
		}else{
			// 有部门ID 需要走自定义sql
			SysDepart sysDepart = sysDepartService.getById(departId);
			pageList = this.baseMapper.queryDepartUserPageList(page, sysDepart.getOrgCode(), username, realname);
		}
		List<SysUser> userList = pageList.getRecords();
		if(userList!=null && userList.size()>0){
			List<String> userIds = userList.stream().map(SysUser::getId).collect(Collectors.toList());
			Map<String, SysUser> map = new HashMap(5);
			if(userIds!=null && userIds.size()>0){
				// 查部门名称
				Map<String,String>  useDepNames = this.getDepNamesByUserIds(userIds);
				userList.forEach(item->{
					//TODO 临时借用这个字段用于页面展示
					item.setOrgCodeTxt(useDepNames.get(item.getId()));
					item.setSalt("");
					item.setPassword("");
					// 去重
					map.put(item.getId(), item);
				});
			}
			pageList.setRecords(new ArrayList<SysUser>(map.values()));
		}
		return pageList;
	}

    @Override
    public IPage<SysUser> getUserInformation(Integer tenantId, String departId, String keyword, Integer pageSize, Integer pageNo) {
        IPage<SysUser> pageList = null;
        // 部门ID不存在 直接查询用户表即可
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(oConvertUtils.isEmpty(departId)){
            LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
            query.eq(SysUser::getStatus,CommonConstant.STATUS_1);
            query.ne(SysUser::getUsername,"_reserve_user_external");

			// 支持租户隔离
			if (tenantId != null) {
				List<String> userIds = userTenantMapper.getUserIdsByTenantId(tenantId);
				if(oConvertUtils.listIsNotEmpty(userIds)){
					query.in(SysUser::getId, userIds);
				}else{
					query.eq(SysUser::getId,"通过租户ID查不到用户");
				}
			}
			
            //排除自己
            query.ne(SysUser::getId,sysUser.getId());
			if(StringUtils.isNotEmpty(keyword)){
				//这个语法可以将or用括号包起来，避免数据查不到
				query.and((wrapper) -> wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname,keyword));
			}
            pageList = sysUserMapper.selectPage(page, query);
        }else{
            // 有部门ID 需要走自定义sql
            SysDepart sysDepart = sysDepartService.getById(departId);
            pageList = this.baseMapper.getUserInformation(page, sysDepart.getOrgCode(), keyword,sysUser.getId());
        }
        return pageList;
    }

	@Override
	public IPage<SysUser> getUserInformation(Integer tenantId, String departId,String roleId, String keyword, Integer pageSize, Integer pageNo, String excludeUserIdList) {
		IPage<SysUser> pageList = null;
		// 部门ID不存在 直接查询用户表即可
		Page<SysUser> page = new Page<>(pageNo, pageSize);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		
		List<String> userIdList = new ArrayList<>();
		if(oConvertUtils.isNotEmpty(excludeUserIdList)){
			userIdList = Arrays.asList(excludeUserIdList.split(SymbolConstant.COMMA));
		}
		if(oConvertUtils.isNotEmpty(departId)){
			// 有部门ID 需要走自定义sql
			SysDepart sysDepart = sysDepartService.getById(departId);
			pageList = this.baseMapper.getProcessUserList(page, sysDepart.getOrgCode(), keyword, tenantId, userIdList);
		} else if (oConvertUtils.isNotEmpty(roleId)) {
			pageList = this.sysUserMapper.selectUserListByRoleId(page, roleId, keyword, tenantId,userIdList);
		} else{
			LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
			query.eq(SysUser::getStatus,CommonConstant.STATUS_1);
			query.ne(SysUser::getUsername,"_reserve_user_external");
			if(oConvertUtils.isNotEmpty(excludeUserIdList)){
				query.notIn(SysUser::getId,Arrays.asList(excludeUserIdList.split(SymbolConstant.COMMA)));
			}
			// 支持租户隔离
			if (tenantId != null) {
				List<String> userIds = userTenantMapper.getUserIdsByTenantId(tenantId);
				if(oConvertUtils.listIsNotEmpty(userIds)){
					query.in(SysUser::getId, userIds);
				}else{
					query.eq(SysUser::getId,"通过租户ID查不到用户");
				}
			}
			
			if(StringUtils.isNotEmpty(keyword)){
				//这个语法可以将or用括号包起来，避免数据查不到
				query.and((wrapper) -> wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname,keyword));
			}
			pageList = sysUserMapper.selectPage(page, query);
		}
		// 批量查询用户的所属部门
		// step.1 先拿到全部的 useids
		// step.2 通过 useids，一次性查询用户的所属部门名字
		List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
		if (userIds.size() > 0) {
			Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
			pageList.getRecords().forEach(item -> item.setOrgCodeTxt(useDepNames.get(item.getId())));
		}
		return pageList;
	}

	@Override
	public List<SysUser> getUsersByDepartTenantId(String departId, Integer tenantId) {
		return baseMapper.getUsersByDepartTenantId(departId,tenantId);
	}

	/**
	 * 升级SpringBoot2.6.6,不允许循环依赖
	 * @param userIds
	 * @return
	 */
	private Map<String, String> getDepNamesByUserIds(List<String> userIds) {
		List<SysUserDepVo> list = sysUserMapper.getDepNamesByUserIds(userIds);

		Map<String, String> res = new HashMap(5);
		list.forEach(item -> {
					res.merge(item.getUserId(), item.getDepartName(), (a, b) -> a + "," + b);
				}
		);
		return res;
	}

}
