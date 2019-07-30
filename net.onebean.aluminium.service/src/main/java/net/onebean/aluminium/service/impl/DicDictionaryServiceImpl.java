package net.onebean.aluminium.service.impl;
import net.onebean.aluminium.service.DicDictionaryService;
import org.springframework.stereotype.Service;
import net.onebean.core.base.BaseBiz;
import net.onebean.aluminium.model.DicDictionary;
import net.onebean.aluminium.dao.DicDictionaryDao;

@Service
public class DicDictionaryServiceImpl extends BaseBiz<DicDictionary, DicDictionaryDao> implements DicDictionaryService {

    @Override
    public Integer findGroupOrderNextNum(String code) {
        return baseDao.findGroupOrderNextNum(code);
    }
}