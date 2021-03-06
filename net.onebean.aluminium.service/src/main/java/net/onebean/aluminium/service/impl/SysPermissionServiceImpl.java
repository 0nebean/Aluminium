package net.onebean.aluminium.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.onebean.core.base.BaseBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.core.query.Condition;
import net.onebean.core.extend.Sort;
import net.onebean.core.form.Parse;
import net.onebean.aluminium.vo.MenuTree;
import net.onebean.aluminium.common.dataPerm.DataPermUtils;
import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.dao.SysPermissionDao;
import net.onebean.aluminium.model.CodeDatabaseTable;
import net.onebean.aluminium.model.SysPermission;
import net.onebean.aluminium.model.SysPermissionRole;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysPermissionRoleService;
import net.onebean.aluminium.service.SysPermissionService;
import net.onebean.aluminium.service.SysUserService;
import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysPermissionServiceImpl extends BaseBiz<SysPermission, SysPermissionDao> implements SysPermissionService {

	@Autowired
	private SysPermissionRoleService sysPermissionRoleService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private DataPermUtils dataPermUtils;

	private static final String CLOUD_ADMIN_USER_TYPE = "root";
	private static final String ADMIN_USER_TYPE = "admin";

	public List<SysPermission> springSecurityFindByAdminUserId(Integer userId) {
		String userIdStr = userId.toString();
		SysUser user = sysUserService.findById(userIdStr);
		if (null != user && (user.getUserType().equals(ADMIN_USER_TYPE) || user.getUserType().equals(CLOUD_ADMIN_USER_TYPE))){
			return this.findAll();
		}
		return baseDao.findByUserId(userIdStr);
	}


	public List<SysPermission> findChildSync(SysUser currentUser) {
		Map<String,Object> dp = getDataPermissionSqlByCurrentLoginUser(currentUser);
		return tree(this.findAll(dp));
	}

	/**
	 * 包装数据列表成树结构
	 * @param original 数据列表
	 * @return List<SysPermission>
	 */
	private List<SysPermission> tree(List<SysPermission> original){
		List<SysPermission> _final = new ArrayList<>();
		for (SysPermission p0:original){
			//如果是根节点,放入最终的结果中剩余数量减一
			if (p0.getIsRoot().equals("1")){
				_final.add(p0);
			}
		}
		treeChild(_final,original);
		return _final;
	}

	/**
	 * 包装方法递归子方法
	 * @param list 数据列表
	 * @param original 元数据
	 */
	private  void treeChild(List<SysPermission> list,List<SysPermission> original){
		List<SysPermission> _final;
		for (SysPermission s : list) {
			if (CollectionUtil.isEmpty(s.getChildList())){
				_final = new ArrayList<>();
				for (SysPermission s1 : original) {
					if (null != s1.getParentId() && null != s.getId() && s1.getParentId().equals(s.getId())){
						_final.add(s1);
					}
				}
				//递归算法
				treeChild(_final,original);
				s.setChildList(_final);
			}
		}
	}

	/**
	 * 查找所有菜单算法排序
	 * @return List<SysPermission>
	 */
	public List<SysPermission>findChildSyncForMenu(){
		Condition param = Condition.parseModelCondition("menuType@string@eq");
		param.setValue("menu");
		Sort sort = new Sort(Sort.ASC,"sort");
		return tree(this.find(null,new ArrayList<Condition>(){{add(param);}},sort));
	}



	public List<MenuTree> findChildAsync(Long parentId,Long selfId,SysUser currentUser){
		List<MenuTree> res = new ArrayList<>();
		Map<String,Object> dp = getDataPermissionSqlByCurrentLoginUser(currentUser);
		List<MenuTree> list = baseDao.findChildAsync(parentId,dp);
		for (MenuTree o : list) {//某些业务场景 节点不能选择自己作为父级节点,故过滤掉所有自己及以下节点
			if (null == selfId || (Parse.toInt(o.getId()) != selfId) || selfId == 1) {
				res.add(o);
			}
		}
		return res;
	}


	public List<MenuTree> permissionToMenuTree(List<SysPermission> before, Long selfId){
		List<MenuTree> treeList = new ArrayList<>();
		MenuTree temp;
		if(CollectionUtil.isNotEmpty(before)){
			for (SysPermission SysPermission: before) {
				if (null == selfId || (Parse.toInt(SysPermission.getId()) != selfId) || selfId == 1) {
					temp = new MenuTree();
					temp.setTitle(SysPermission.getDescritpion());
					temp.setId(SysPermission.getId());
					temp.setMenuType(SysPermission.getMenuType());
					temp.setName(SysPermission.getName());
					temp.setSort(SysPermission.getSort());
					temp.setUrl(SysPermission.getUrl());
					if (CollectionUtil.isNotEmpty(SysPermission.getChildList())) {
						temp.setType(MenuTree.TYPE_FOLDER);
						temp.setChildList(permissionToMenuTree(SysPermission.getChildList(),selfId));
					} else {
						temp.setType(MenuTree.TYPE_ITEM);
					}
					treeList.add(temp);
				}
			}
		}
		return  treeList;
	}

	public List<MenuTree> permissionToMenuTreeForRole(List<SysPermission> before, Long selfId,Long roleId){
		List<MenuTree> treeList = new ArrayList<>();
		MenuTree temp;
		Map<String,Object> attr;
		List<SysPermissionRole> prList = sysPermissionRoleService.getRolePremissionByRoleId(roleId);
		if(CollectionUtil.isNotEmpty(before)){
			for (SysPermission SysPermission: before) {
				if (null == selfId || (Parse.toInt(SysPermission.getId()) != selfId) || selfId == 1) {
					temp = new MenuTree();
					attr = new HashMap<>();
					for (SysPermissionRole sysPermissionRole : prList) {
						if (sysPermissionRole.getPermissionId().equals(SysPermission.getId())){
							attr.put("classNames","selected-item");
						}
					}

					temp.setAttr(attr);
					temp.setTitle(SysPermission.getDescritpion());
					temp.setId(SysPermission.getId());
					temp.setMenuType(SysPermission.getMenuType());
					temp.setName(SysPermission.getName());
					temp.setSort(SysPermission.getSort());
					temp.setUrl(SysPermission.getUrl());
					if (CollectionUtil.isNotEmpty(SysPermission.getChildList())) {
						temp.setType(MenuTree.TYPE_FOLDER);
						temp.setChildList(permissionToMenuTreeForRole(SysPermission.getChildList(),selfId,roleId));
					} else {
						temp.setType(MenuTree.TYPE_ITEM);
					}
					treeList.add(temp);
				}
			}
		}
		return  treeList;
	}


	@Override
	public List<SysPermission> getCurrentLoginUserHasPermission(List<SysPermission> list, JSONArray currentPer) {
		return iterChildList(list,currentPer);
	}

	private List<SysPermission> iterChildList(List<SysPermission> list,JSONArray currentPer){
		List<SysPermission> result  = new ArrayList<>();
		for (SysPermission parent : list) {
			currentPer.stream().map(o -> {
				JSONObject temp = (JSONObject)o;
				if (parent.getName().equals(temp.getString("authority"))){
					if (CollectionUtil.isNotEmpty(parent.getChildList())){
						parent.setChildList(iterChildList(parent.getChildList(),currentPer));
					}
					result.add(parent);
				}
				return o;
			}).collect(Collectors.toList());
		}
		return result;
	}

	@Override
	public void deleteSelfAndChildById(Long id) {
		baseDao.deleteSelfAndChildById(id);
	}

	@Override
	public Integer findChildOrderNextNum(Long parentId) {
		Integer res = baseDao.findChildOrderNextNum(parentId);
		return (null == res)?0:res;
	}

	@Override
	public void save(SysPermission entity) {
		super.save(entity);
		entity.setParentIds(getParentMenuIdsNotEmpty(entity.getId()));
		super.save(entity);
	}

	@Override
	public void saveBatch(List<SysPermission> entities) {
		super.saveBatch(entities);
		for (SysPermission entity : entities) {
			entity.setParentIds(getParentMenuIdsNotEmpty(entity.getId()));
		}
		super.saveBatch(entities);
	}

	@Override
	public Integer update(SysPermission entity) {
		entity.setParentIds(getParentMenuIdsNotEmpty(entity.getId()));
		return super.update(entity);
	}

	@Override
	public Integer updateBatch(SysPermission entity, List<Long> ids) {
		entity.setParentIds(getParentMenuIdsNotEmpty(entity.getId()));
		return super.updateBatch(entity, ids);
	}

	@Override
	public void updateBatch(List<SysPermission> entities) {
		for (SysPermission entity : entities) {
			entity.setParentIds(getParentMenuIdsNotEmpty(entity.getId()));
		}
		super.updateBatch(entities);
	}

	protected String getParentMenuIdsNotEmpty(Long id){
		String res = baseDao.getParentMenuIds(id);
		if (StringUtils.isEmpty(res)){
			return  null;
		}
		return  res;
	}

	@Override
	public void generatePermission(CodeDatabaseTable table) {
		SysPermission permission = new SysPermission();
		permission.setName(table.getPremName());
		permission.setParentId(table.getParentMenuId());
		permission.setIcon(table.getMenuIcon());
		permission.setSort(findChildOrderNextNum(permission.getParentId()));
		permission.setMenuType("menu");
		permission.setDescritpion(table.getDescription());
		permission.setUrl("/"+StringUtils.getMappingStr(table.getTableName())+"/preview");
		this.save(permission);

		String [] premArr = {"_PREVIEW","_ADD","_VIEW","_EDIT","_SAVE","_DELETE","_LIST"};
		for (int i = 0; i < premArr.length; i++) {
			SysPermission temp = new SysPermission();
			try {
				BeanUtils.copyProperties(temp,permission);
			} catch (Exception e) {
				throw new BusinessException(ErrorCodesEnum.REF_ERROR.code(),ErrorCodesEnum.REF_ERROR.msg());
			}
			temp.setName(temp.getName()+premArr[i]);
			String descritpion = "";
			switch(premArr[i]){
				case "_ADD":
					descritpion = "新增";
					break;
				case "_VIEW":
					descritpion = "查看";
					break;
				case "_EDIT":
					descritpion = "编辑";
					break;
				case "_SAVE":
					descritpion = "保存";
					break;
				case "_DELETE":
					descritpion = "删除";
					break;
				case "_LIST":
					descritpion = "列表数据";
					break;
				case "_PREVIEW":
					descritpion = "列表页面";
					break;
				default:
					break;
			}
			temp.setDescritpion(temp.getDescritpion()+descritpion);
			temp.setUrl("");
			temp.setIcon("");
			temp.setMenuType("url");
			temp.setSort(i);
			temp.setId(null);
			temp.setParentId(permission.getId());
			this.save(temp);
		}

	}

	@Override
	public Boolean delPerm(Object id) {
		this.deleteSelfAndChildById(Parse.toLong(id));
		sysPermissionRoleService.deteleByPermissionId(Parse.toLong(id));
		return true;
	}

	@Override
	public Boolean urlRepeat(String reg, Long id) {
		Condition param;
		if (reg.startsWith("PERM_")) {
			param = Condition.parseModelCondition("name@string@eq");
		} else {
			param = Condition.parseModelCondition("url@string@eq");
		}
		param.setValue(reg);
		List<SysPermission> list = this.find(null, param);
		if (CollectionUtil.isEmpty(list)) {
			return true;
		} else {
			if (id == null) {
				return false;
			} else {
				return (list.get(0).getId().equals(id));
			}
		}
	}

	@Override
	public Boolean savePermissionRole(String premIds, String roleId) {
		sysPermissionRoleService.deteleByRoleId(Parse.toLong(roleId));
		if (StringUtils.isNotEmpty(premIds)) {
			sysPermissionRoleService.insertBatch(premIds, roleId);
		}
		return true;
	}

	private Map<String ,Object> getDataPermissionSqlByCurrentLoginUser(SysUser currentUser){
		StringBuilder join = new StringBuilder();
		join.append(" LEFT JOIN sys_permission_role spr on t.id = spr.permission_id");
		join.append(" LEFT JOIN sys_role r on spr.role_id = r.id");
		join.append(" LEFT JOIN sys_role_user sru on sru.sys_role_id = r.id");
		join.append(" LEFT JOIN sys_user u on u.id = sru.sys_user_id");
		join.append(" LEFT JOIN sys_organization o on u.org_id = o.id");
		Map<String,Object> dp = dataPermUtils.dataPermFilter(currentUser,"o","t",join.toString());
		StringBuilder sb = new StringBuilder();
		if (null != dp.get("sql")){
			String sql = dp.get("sql").toString();
			sb.append(sql);
		}
		sb.append(" AND u.`id` = ");
		sb.append(currentUser.getId());
		sb.append(" AND spr.is_deleted = '0'");
		sb.append(" AND r.is_deleted = '0'");
		sb.append(" AND sru.is_deleted = '0'");
		sb.append(" AND o.is_deleted = '0'");
		sb.append(" AND u.is_deleted = '0'");
		dp.put("sql",sb.toString());
		return dp;
	}
}