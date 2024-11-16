package com.artineer.artineer_renewal.service;


import com.artineer.artineer_renewal.entity.IntegratedArticle;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.repository.IntegratedArticleRepository;
import com.artineer.artineer_renewal.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntegratedArticleService {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private IntegratedArticleRepository integratedArticleRepository;

    public Page<IntegratedArticle> findAllArticlesByQuery(Class<?> entityClass, String queryType, String query, Pageable pageable) {
        List<Class<?>> entityClasses = new ArrayList<>();
        entityClasses.add(entityClass);

        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(entityClasses, queryType, query, pageable);
        return integratedList;
    }


    public Page<IntegratedArticle> findAllArticlesByQuery(List<Class<?>> entityClasses, String queryType, String query, Pageable pageable) {
        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(entityClasses, queryType, query, pageable);
        return integratedList;
    }
}
