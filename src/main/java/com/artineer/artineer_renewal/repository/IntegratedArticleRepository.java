package com.artineer.artineer_renewal.repository;

import com.artineer.artineer_renewal.entity.Comment;
import com.artineer.artineer_renewal.entity.IntegratedArticle;
import com.artineer.artineer_renewal.entity.Notice;
import com.artineer.artineer_renewal.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.aspectj.weaver.ast.Not;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Repository
public class IntegratedArticleRepository {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public IntegratedArticleRepository(UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    
    // db 구조에 종속적
    // db의 union 을 이용하여 여러 테이블에서의 튜플들을 합성한다.
    // todo 각 게시판 테이블에 네임 속성이 있기는 한데, 나중에 완전히 제거할 예정이니 별도의 쿼리를 만들던지
    // note 테이블은 테이블 컬럼순서가 달라 작동 안됨
    public Page<IntegratedArticle> getIntegratedArticles(List<Class<?>> classList, String queryType, String queryValue, Pageable pageable) {
        StringBuilder sql = new StringBuilder();

        // native sql 작성문
        for (int i = 0; i < classList.size(); i++) {
            String tableName = classList.get(i).getSimpleName().toLowerCase();
            sql.append("SELECT * FROM ");

            // db 테이블명과 엔티티 클래스명이 달라서...
            if (tableName.equals("greeting")) sql.append("hello");
            else sql.append(tableName);

            if (!queryValue.isBlank()) sql.append(" where ").append(queryType).append(" like '%").append(queryValue).append("%' ");

            if (i < classList.size() - 1) sql.append(" UNION ");
        }

        // 쿼리문을 만든다. 반환은 Object 배열로 한다.
        Query query = entityManager.createNativeQuery(sql.toString(), Object.class);

        List<Object[]> resultList = query.getResultList();
        List<IntegratedArticle> integratedList = new ArrayList<>();


        // 각 직렬화된? 값들을 직접 수동으로 통합게시글 객체에 담는다.
        for (Object[] row : resultList) {
            Integer id = ((Number) row[0]).intValue();
            User user = userRepository.findByUsername((String) row[1] );
            String name = row[2].toString();
            String title = row[3].toString();
            String content = row[4].toString();
            Integer hit = ((Number) row[5]).intValue();
            Integer year = ((Number) row[6]).intValue();
            String file = ((String) row[7]);
            Integer comment = ((Number) row[8]).intValue();
            List<Comment> comments = commentRepository.findAllByBbsNo(id);
            String regdate = ((String) row[9]);
            String ip = ((String) row[10]);

            IntegratedArticle article = new IntegratedArticle(
                    id.longValue(),
                    title,
                    content,
                    hit,
                    file,
                    comment,
                    comments,
                    regdate,
                    ip,
                    user,
                    name,
                    year
            );
            integratedList.add(article);
        }


        // 생성일자가 문자열이라...
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");


        // 글어온 값들을 시간 순으로 정렬한다. db에 쓰레기 값이 좀 있어서 예외가 발생했다.
        integratedList.sort(
                Comparator.<IntegratedArticle, LocalDateTime>comparing(a -> {
                    try {
                        return LocalDateTime.parse(a.getRegdate(), formatter );
                    }
                    catch (DateTimeParseException e) {
                        return LocalDateTime.parse("0001-01-01 (00:00)", formatter );
                    }
                } ).reversed()
        );


        // 위까지의 결과는 모든 조건에 맞는 쿼리를 했다.
        // 페이지네이션을 위해 직접 기본 로직을 작성한다.
        // 아래 주석 신경 쓰지 않는다. 어차피 url 쿼리 이상하게 날리는 거 아니면 접근할 일 없을 듯
//        int start = Math.min( (int) pageable.getOffset(), pageable.getPageSize()%integratedList.size() );
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), integratedList.size());

        List<IntegratedArticle> pagedContent;
        try {
            pagedContent = integratedList.subList(start, end);
        } catch (Exception e) {
            throw new InvalidDataAccessApiUsageException("존재하지 않는 페이지 입니다.");
        }

        // 일반적으로 각각의 레포에서는 페이지로 연속자료형을 반환하여 교체를 고려하여 페이지로 반환한다.
        return new PageImpl<>(pagedContent, pageable, integratedList.size());
    }



    //    public Page<IntegratedArticle> getIntegratedArticles(String title, Pageable pageable) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<IntegratedArticle> query = cb.createQuery(IntegratedArticle.class);
//        Root<IntegratedArticle> root = query.from(IntegratedArticle.class);
//
//        // 제목(title)에 검색어가 포함된 경우
//        Predicate titlePredicate = cb.like(root.get("title"), "%" + title + "%");
//
//        // 쿼리 구성
//        query.select(root)
//                .where(titlePredicate)
//                .orderBy(cb.desc(root.get("regdate")));
//
//        // 쿼리 실행
//        TypedQuery<IntegratedArticle> typedQuery = entityManager.createQuery(query);
//        typedQuery.setFirstResult((int) pageable.getOffset());
//        typedQuery.setMaxResults(pageable.getPageSize());
//
//        List<IntegratedArticle> results = typedQuery.getResultList();
//        long totalCount = getTotalCount(title);
//
//        return new PageImpl<>(results, pageable, totalCount);
//    }
//
//
//
//
//    private long getTotalCount(String title) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Root<IntegratedArticle> root = countQuery.from(IntegratedArticle.class);
//
//        Predicate titlePredicate = cb.like(root.get("title"), "%" + title + "%");
//
//        countQuery.select(cb.count(root)).where(titlePredicate);
//
//        return entityManager.createQuery(countQuery).getSingleResult();
//    }

}
