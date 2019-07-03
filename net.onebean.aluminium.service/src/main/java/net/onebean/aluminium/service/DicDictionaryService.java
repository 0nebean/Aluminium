package net.onebean.aluminium.service;
import net.onebean.core.IBaseBiz;
import net.onebean.aluminium.model.DicDictionary;

public interface DicDictionaryService extends IBaseBiz<DicDictionary> {
    /**
     * 根据code  查询组下一个排序值
     * @param code
     * @return
     */
    Integer findGroupOrderNextNum(String code);
}