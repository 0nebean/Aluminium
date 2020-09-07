package net.onebean.aluminium.action.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Condition;
import net.onebean.core.query.Pagination;
import net.onebean.aluminium.common.dataPerm.DataPermUtils;
import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.core.BaseController;
import net.onebean.aluminium.model.CodeDatabaseTable;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.security.MyPasswordEncoder;
import net.onebean.aluminium.security.SpringSecurityUtil;
import net.onebean.aluminium.service.SysUserService;
import net.onebean.util.DateUtils;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * 用户管理
 *
 * @author Heisenberg
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends BaseController<SysUser, SysUserService> {

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;
    @Autowired
    private DataPermUtils dataPermUtils;

    private static final String CLOUD_ADMIN_USER = "root";
    private static final String DISABLE_USER_TYPE = "root,admin";

    /**
     * 编辑页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping("edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        String cUserType = Optional.ofNullable(currentUser).map(SysUser::getUserType).orElse(null);
        if (StringUtils.isNotEmpty(cUserType) && !cUserType.equals(CLOUD_ADMIN_USER)) {
            model.addAttribute("rootAdminEdit", DISABLE_USER_TYPE);
        }
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 查看页面
     *
     * @param model modelAndView
     * @param id    主键
     * @return view
     */
    @RequestMapping("view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_VIEW')")
    public String view(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("view", true);
        return getView("detail");
    }

    /**
     * 新增页面
     *
     * @param model  modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping("add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_ADD')")
    public String add(Model model, SysUser entity) {
        SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
        String cUserType = Optional.ofNullable(currentUser).map(SysUser::getUserType).orElse(null);
        if (StringUtils.isNotEmpty(cUserType) && !cUserType.equals(CLOUD_ADMIN_USER)) {
            model.addAttribute("rootAdminEdit", DISABLE_USER_TYPE);
        }
        model.addAttribute("add", true);
        model.addAttribute("entity", entity);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @return BaseResponse<SysUser>
     */
    @RequestMapping("save")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_SAVE')")
    public BaseResponse<SysUser> add(@RequestBody SysUser entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<SysUser> response = new BaseResponse<>();
        try {
            logger.debug("method add entity = " + JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
            entity = loadOperatorData(entity);
            baseService.save(entity);
            response = response.ok(entity);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method add BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method add catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 保存
     * @param entity 实体
     * @return BaseResponse<SysUser>
     */
    @RequestMapping("resetPassword")
    @ResponseBody
    public BaseResponse<Boolean> resetPassword(@RequestBody SysUser entity) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method reSetPassword entity = " + JSON.toJSONString(entity, SerializerFeature.WriteMapNullValue));
            String password = Optional.of(entity).map(SysUser::getPassword).orElse("");
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            password = myPasswordEncoder.encode(password);
            response = response.ok(baseService.resetPassword(currentUser,password));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method reSetPassword BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method reSetPassword catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 预览列表页面
     *
     * @return view
     */
    @RequestMapping("preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_USER_PREVIEW')")
    public String preview() {
        return getView("list");
    }


    /**
     * 列表数据
     *
     * @param request 参数体
     * @return BasePaginationResponse<CodeDatabaseTable>
     */
    @RequestMapping(value = "list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_LIST')")
    public BasePaginationResponse<CodeDatabaseTable> list(@RequestBody BasePaginationRequest<String> request) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<CodeDatabaseTable> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = " + JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC, "id"));
            SysUser currentUser = SpringSecurityUtil.getCurrentLoginUser();
            String join = "LEFT JOIN sys_organization o ON o.`id` = t.org_id";
            initData(sort, page, cond, dataPermUtils.dataPermFilter(currentUser, "o", "t", join));
            dicCoverList(null, "dic@SF$isLock", "dic@YHLX$userType", "date@createTime$");
            response = response.ok(dataList, page);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method list BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method list catch Exception e = ", e);
        }
        return response;
    }


    /**
     * 删除数据库模型及其关联字段
     *
     * @param id 主键
     * @return PageResult<Boolean>
     */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_DELETE')")
    public BaseResponse<Boolean> delete(@PathVariable("id") Object id) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = response.ok(baseService.deleteUser(id));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method delete BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method delete catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "resetpassword/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_RESET_PASSWORD')")
    public BaseResponse<Integer> resetpassword(@PathVariable("id") Object id) {
        logger.info("method resetpassword access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Integer> response = new BaseResponse<>();
        try {
            logger.debug("method resetpassword id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            SysUser entity = baseService.findById(id);
            entity.setPassword(myPasswordEncoder.encode("123456"));
            response = response.ok(baseService.update(entity));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method resetpassword BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method resetpassword catch Exception e = ", e);
        }
        return response;
    }

    /**
     * 账户设置
     *
     * @param model modelAndView
     * @return view
     */
    @RequestMapping(value = "setting")
    @PreAuthorize("isAuthenticated()")
    public String setting(Model model) {
        SysUser entity = SpringSecurityUtil.getCurrentLoginUser();
        model.addAttribute("entity", entity);
        model.addAttribute("edit", true);
        return getView("setting");
    }

    /**
     * 设置密码
     *
     * @param model modelAndView
     * @return view
     */
    @RequestMapping(value = "setpassword")
    @PreAuthorize("isAuthenticated()")
    public String setpassword(Model model) {
        model.addAttribute("entity", SpringSecurityUtil.getCurrentLoginUser());
        return getView("setpassword");
    }

    /**
     * 根据用户'orgid'查找用户
     */
    @RequestMapping(value = "finduserbyorgid/{orgId}")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_USER_FIND_BY_ORGID')")
    public BasePaginationResponse<SysUser> findUserByOrgId(@PathVariable("orgId") String orgId) {
        logger.info("method findUserByOrgId access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysUser> response = new BasePaginationResponse<>();
        try {
            logger.debug("method findUserByOrgId orgId = " + JSON.toJSONString(orgId, SerializerFeature.WriteMapNullValue));
            Condition param = Condition.parseModelCondition("orgId@string@eq");
            param.setValue(orgId);
            response = response.ok(baseService.find(null, param));
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method findUserByOrgId BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method findUserByOrgId catch Exception e = ", e);
        }
        return response;
    }

}
