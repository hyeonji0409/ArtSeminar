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


    /**
     * DB에서 여러 게시판 튜플들을 읽어오는 메서드
     * !!주의!! 다음 매개변수 설명에 주의하여 입력하세요.
     * @param entityClass 엔티티 클래스
     * @param queryType 실제 DB Table에 있는 속성명을 입력
     * @param query 검색할 내용을 자유롭게 작성
     * @param pageable 페이지네이션 관련 정보
     */
    public Page<IntegratedArticle> findAllArticlesByQuery(Class<?> entityClass, String queryType, String query, Pageable pageable) {
        List<Class<?>> entityClasses = new ArrayList<>();
        entityClasses.add(entityClass);

        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(entityClasses, queryType, query, pageable);
        return integratedList;
    }

    /**
     * DB에서 여러 게시판 튜플들을 읽어오는 메서드
     * !!주의!! 다음 매개변수 설명에 주의하여 입력하세요.
     * @param entityClasses 엔티티 클래스를 담는 리스트
     * @param queryType 실제 DB Table에 있는 속성명을 입력
     * @param query 검색할 내용을 자유롭게 작성
     * @param pageable 페이지네이션 관련 정보
     */
    public Page<IntegratedArticle> findAllArticlesByQuery(List<Class<?>> entityClasses, String queryType, String query, Pageable pageable) {
        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(entityClasses, queryType, query, pageable);
        return integratedList;
    }
}
