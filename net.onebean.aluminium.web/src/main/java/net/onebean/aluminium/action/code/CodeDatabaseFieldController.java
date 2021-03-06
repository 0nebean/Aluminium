package net.onebean.aluminium.action.code;


import net.onebean.aluminium.core.BaseController;
import net.onebean.aluminium.model.CodeDatabaseField;
import net.onebean.aluminium.service.CodeDatabaseFieldService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据库模型字段管理
 * @author 0neBean
 */
@Controller
@RequestMapping("databasefield")
public class CodeDatabaseFieldController extends BaseController<CodeDatabaseField, CodeDatabaseFieldService> {
}