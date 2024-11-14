package com.artineer.artineer_renewal.service;


import com.artineer.artineer_renewal.entity.Gallery;
import com.artineer.artineer_renewal.entity.IntegratedArticle;
import com.artineer.artineer_renewal.entity.Minutes;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.repository.GalleryRepository;
import com.artineer.artineer_renewal.repository.IntegratedArticleRepository;
import com.artineer.artineer_renewal.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class IntegratedArticleService {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private IntegratedArticleRepository integratedArticleRepository;


    public Page<IntegratedArticle> getNoticesByQuery(List<Class<?>> entityClasses, String query, Pageable pageable) {
//
////        int size = 0;
        List<Notice> noticePage = noticeRepository.findAllByTitleContaining(query);
        List<Gallery> galleryPage = galleryRepository.findAllByTitleContaining(query);
//////        size += (int) noticePage.getTotalElements();
//////        size += (int) galleryPage.getTotalElements();
////
////
////        integratedList.addAll(noticePage);
////        integratedList.addAll(galleryPage);
////        integratedList.sort(
////                Comparator.comparing(
////                        obj -> ((YourClassType) obj).getRegdate(), // 적절한 클래스와 메서드명으로 변경
////                        Comparator.nullsLast(LocalDateTime::compareTo)
////                ).reversed()
////        );
//
//
//

//        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(title, pageable);
        Page<IntegratedArticle> integratedList = integratedArticleRepository.getIntegratedArticles(query, entityClasses, pageable);
        return integratedList;

//        integratedList.addAll(noticePage);
//        integratedList.addAll(galleryPage);

//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), integratedList.getSize());

//        List<IntegratedArticle> pagedContent = integratedList.subList(start, end);
//        return new PageImpl<>(pagedContent, pageable, integratedList.size());
    }
}
