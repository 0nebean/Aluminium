package net.onebean.aluminium.action.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.onebean.core.base.BasePaginationRequest;
import net.onebean.core.base.BasePaginationResponse;
import net.onebean.core.base.BaseResponse;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.core.BaseController;
import net.onebean.aluminium.model.SysAdminAccessIpRecord;
import net.onebean.aluminium.service.SysAdminAccessIpRecordService;
import net.onebean.util.DateUtils;
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
* @author 0neBean
* @description 后台访问记录流水 controller
* @date 2020-07-12 01:38:43
*/
@Controller
@RequestMapping("sysadminaccessiprecord")
public class SysAdminAccessIpRecordController extends BaseController<SysAdminAccessIpRecord,SysAdminAccessIpRecordService> {

    /**
     * 预览列表页面
     * @return view
     */
    @RequestMapping(value = "preview")
    @Description(value = "预览列表页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_PREVIEW')")
    public String preview() {
        return getView("list");
    }


    /**
     * 新增页面
     * @param model modelAndView
     * @param entity 实体
     * @return view
     */
    @RequestMapping(value = "add")
    @Description(value = "新增页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_ADD')")
    public String add(Model model, SysAdminAccessIpRecord entity) {
        model.addAttribute("add", true);
        model.addAttribute("entity", entity);
        return getView("detail");
    }

    /**
     * 查看页面
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping(value = "view/{id}")
    @Description(value = "查看页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_VIEW')")
    public String view(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("view", true);
        return getView("detail");
    }

    /**
     * 编辑页面
     * @param model modelAndView
     * @param id 主键
     * @return view
     */
    @RequestMapping(value = "edit/{id}")
    @Description(value = "编辑页面")
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_EDIT')")
    public String edit(Model model, @PathVariable("id") Object id) {
        model.addAttribute("entity", baseService.findById(id));
        model.addAttribute("edit", true);
        return getView("detail");
    }

    /**
     * 保存
     * @param entity 实体
     * @return BaseResponse<SysAdminAccessIpRecord>
    */
    @RequestMapping("save")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_SAVE')")
    public BaseResponse<SysAdminAccessIpRecord> add(@RequestBody SysAdminAccessIpRecord entity) {
    logger.info("method add access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
    BaseResponse<SysAdminAccessIpRecord> response = new BaseResponse<>();
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
    * 根据ID删除
    * @param id 主键
    * @return PageResult<SysAdminAccessIpRecord>
    */
    @RequestMapping(value = "delete/{id}")
    @Description(value = "删除")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_DELETE')")
    public BaseResponse<Integer> delete(@PathVariable("id") Object id) {
        logger.info("access" + DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BaseResponse<Integer> response = new BaseResponse<>();
        try {
            logger.debug("method delete id = " + JSON.toJSONString(id, SerializerFeature.WriteMapNullValue));
            response = response.ok(baseService.deleteById(id));
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
    * 列表数据
    * @param request 参数体
    * @return BasePaginationResponse<SysAdminAccessIpRecord>
    */
    @RequestMapping("list")
    @ResponseBody
    @PreAuthorize("hasPermission('$everyone','PERM_ADMIN_ACCESS_IP_RECORD_LIST')")
    public BasePaginationResponse<SysAdminAccessIpRecord> list (@RequestBody BasePaginationRequest<String> request){
        logger.info("access"+ DateUtils.getNowyyyy_MM_dd_HH_mm_ss());
        BasePaginationResponse<SysAdminAccessIpRecord> response = new BasePaginationResponse<>();
        try {
            logger.debug("method list request = "+ JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            String cond = Optional.ofNullable(request).map(BasePaginationRequest::getData).orElse("");
            Pagination page = Optional.ofNullable(request).map(BasePaginationRequest::getPage).orElse(new Pagination());
            Sort sort = Optional.ofNullable(request).map(BasePaginationRequest::getSort).orElse(new Sort(Sort.DESC,"id"));
            initData(sort,page,cond);
            dicCoverList(null, "dic@HTDLZT$loginStatus");
            response = response.ok(dataList,page);
        } catch (BusinessException e) {
            response.setErrCode(e.getCode());
            response.setErrMsg(e.getMsg());
            logger.info("method list BusinessException ex = ", e);
        } catch (Exception e) {
            response.setErrCode(ErrorCodesEnum.OTHER.code());
            response.setErrMsg(ErrorCodesEnum.OTHER.msg());
            logger.error("method list catch Exception e = ",e);
        }
        return response;
    }

}
