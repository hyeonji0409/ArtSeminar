

(function() {
  "use strict";

  /**
   * Apply .scrolled class to the body as the page is scrolled down
   */
  function toggleScrolled() {
    const selectBody = document.querySelector('body');
    const selectHeader = document.querySelector('#header');
    if (!selectHeader.classList.contains('scroll-up-sticky') && !selectHeader.classList.contains('sticky-top') && !selectHeader.classList.contains('fixed-top')) return;
    window.scrollY > 100 ? selectBody.classList.add('scrolled') : selectBody.classList.remove('scrolled');
  }

  document.addEventListener('scroll', toggleScrolled);
  window.addEventListener('load', toggleScrolled);

  /**
   * Mobile nav toggle
   */
  document.querySelector('.mobile-nav-toggle').addEventListener('click', function() {
    const body = document.body;
    body.classList.toggle('mobile-nav-active'); // 모바일 네비게이션의 활성화 상태 토글

    const toggleIcon = document.querySelector('.mobile-nav-toggle i');
    toggleIcon.classList.toggle('bi-list'); // 햄버거 아이콘

    // 모바일 네비게이션이 활성화되면 슬라이드 효과를 주도록 함
    const navMenu = document.querySelector('.navmenu');
    navMenu.classList.toggle('mobile-nav-active');
  });

  /**
   * Hide mobile nav on same-page/hash links
   */
  document.querySelectorAll('#navmenu a').forEach(navmenu => {
    navmenu.addEventListener('click', () => {
      if (document.querySelector('.mobile-nav-active')) {
        mobileNavToogle(); // 네비게이션이 열려 있으면 닫기
      }
    });
  });

  /**
   * Toggle mobile nav dropdowns
   */
  document.querySelectorAll('.navmenu .toggle-dropdown').forEach(navmenu => {
    navmenu.addEventListener('click', function(e) {
      e.preventDefault();
      this.parentNode.classList.toggle('active');
      this.parentNode.nextElementSibling.classList.toggle('dropdown-active');
      e.stopImmediatePropagation();
    });
  });

  /**
   * Toggle mobile nav menu
   */
  const mobileNavToggle = document.querySelector('.mobile-nav-toggle');
  const navMenu = document.querySelector('.navmenu');

  mobileNavToggle.addEventListener('click', function() {
    // 메뉴가 열릴 때마다 드롭다운 메뉴 상태 초기화
    if (navMenu.classList.contains('mobile-nav-active')) {
      // 메뉴가 닫히면 드롭다운 상태를 초기화
      document.querySelectorAll('.navmenu .dropdown').forEach(dropdown => {
        dropdown.classList.remove('active');
        dropdown.querySelector('ul').classList.remove('dropdown-active');
      });
    }
    // 네비게이션 메뉴를 토글
    navMenu.classList.toggle('mobile-nav-active');
  });



   // document.getElementById('login-button').addEventListener('click', function(event) {
   //     event.preventDefault();
   //     var dropdownMenu = document.getElementById('dropdown-menu');
   //     if (dropdownMenu.style.display === 'none' || dropdownMenu.style.display === '') {
   //         dropdownMenu.style.display = 'block';
   //     } else {
   //         dropdownMenu.style.display = 'none';
   //     }
   // });



  /**
   * Preloader
   */
  const preloader = document.querySelector('#preloader');
  if (preloader) {
    window.addEventListener('load', () => {
      preloader.remove();
    });
  }

  /**
   * Scroll top button
   */
  let scrollTop = document.querySelector('.scroll-top');

  function toggleScrollTop() {
    if (scrollTop) {
      window.scrollY > 100 ? scrollTop.classList.add('active') : scrollTop.classList.remove('active');
    }
  }
  scrollTop.addEventListener('click', (e) => {
    e.preventDefault();
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  });

  window.addEventListener('load', toggleScrollTop);
  document.addEventListener('scroll', toggleScrollTop);

  /**
   * Animation on scroll function and init
   */
  function aosInit() {
    AOS.init({
      duration: 600,
      easing: 'ease-in-out',
      once: true,
      mirror: false
    });
  }
  window.addEventListener('load', aosInit);

  /**
   * Initiate glightbox
   */
  const glightbox = GLightbox({
    selector: '.glightbox'
  });

  /**
   * Open Artineer blog link in new tab
   */
  document.addEventListener('DOMContentLoaded', () => {
    const blogLink = document.querySelector('.btn-watch-video');

    if (blogLink) {
      blogLink.addEventListener('click', (e) => {
        e.preventDefault(); // 기본 링크 동작 방지
        window.open(blogLink.href, '_blank'); // 새 탭에서 링크 열기
      });
    }
  });

  /**
   * Init swiper sliders
   */
  function initSwiper() {
    document.querySelectorAll(".init-swiper").forEach(function(swiperElement) {
      let config = JSON.parse(
        swiperElement.querySelector(".swiper-config").innerHTML.trim()
      );

      if (swiperElement.classList.contains("swiper-tab")) {
        initSwiperWithCustomPagination(swiperElement, config);
      } else {
        new Swiper(swiperElement, config);
      }
    });
  }

  window.addEventListener("load", initSwiper);


  /**
   * Init isotope layout and filters
   */
  document.querySelectorAll('.isotope-layout').forEach(function(isotopeItem) {
    let layout = isotopeItem.getAttribute('data-layout') ?? 'masonry';
    let filter = isotopeItem.getAttribute('data-default-filter') ?? '*';
    let sort = isotopeItem.getAttribute('data-sort') ?? 'original-order';

    let initIsotope;
    imagesLoaded(isotopeItem.querySelector('.isotope-container'), function() {
      initIsotope = new Isotope(isotopeItem.querySelector('.isotope-container'), {
        itemSelector: '.isotope-item',
        layoutMode: layout,
        filter: filter,
        sortBy: sort
      });
    });

    isotopeItem.querySelectorAll('.isotope-filters li').forEach(function(filters) {
      filters.addEventListener('click', function() {
        isotopeItem.querySelector('.isotope-filters .filter-active').classList.remove('filter-active');
        this.classList.add('filter-active');
        initIsotope.arrange({
          filter: this.getAttribute('data-filter')
        });
        if (typeof aosInit === 'function') {
          aosInit();
        }
      }, false);
    });

  });

  /**
   * Correct scrolling position upon page load for URLs containing hash links.
   */
  window.addEventListener('load', function(e) {
    if (window.location.hash) {
      if (document.querySelector(window.location.hash)) {
        setTimeout(() => {
          let section = document.querySelector(window.location.hash);
          let scrollMarginTop = getComputedStyle(section).scrollMarginTop;
          window.scrollTo({
            top: section.offsetTop - parseInt(scrollMarginTop),
            behavior: 'smooth'
          });
        }, 100);
      }
    }
  });

  /**
     * FadeOut Image Slider
  */

  document.addEventListener("DOMContentLoaded", function() {
      const slides = document.querySelectorAll('#slide3 ul li');
      const dots = document.querySelectorAll('.slider__dot .dot');
      let currentIndex = 0;
      let slideInterval;

      function changeSlide(index) {
          slides.forEach(slide => slide.classList.remove('active'));
          dots.forEach(dot => dot.classList.remove('active'));
          slides[index].classList.add('active');
          dots[index].classList.add('active');
      }

      function nextSlide() {
          currentIndex = (currentIndex + 1) % slides.length;
          changeSlide(currentIndex);
      }

      function prevSlide() {
          currentIndex = (currentIndex - 1 + slides.length) % slides.length;
          changeSlide(currentIndex);
      }

      function goToSlide(index) {
          currentIndex = index;
          changeSlide(currentIndex);
      }

      // 자동 슬라이드 기능
      function startAutoSlide() {
          slideInterval = setInterval(nextSlide, 6000);  // 6초마다 자동 전환
      }

      // 슬라이드 정지 (사용자가 버튼 클릭 시)
      function stopAutoSlide() {
          clearInterval(slideInterval);
      }



      // 닷 버튼 클릭 이벤트
      dots.forEach((dot, index) => {
          dot.addEventListener('click', function(e) {
              e.preventDefault();
              stopAutoSlide();
              goToSlide(index);
              startAutoSlide();
          });
      });

      // 페이지 로드 시 첫 번째 슬라이드 활성화
      changeSlide(currentIndex);

      // 자동 슬라이드 시작
      startAutoSlide();
  });



  /**
   * Navmenu Scrollspy
   */
  let navmenulinks = document.querySelectorAll('.navmenu a');

  function navmenuScrollspy() {
    navmenulinks.forEach(navmenulink => {
      if (!navmenulink.hash) return;
      let section = document.querySelector(navmenulink.hash);
      if (!section) return;
      let position = window.scrollY + 200;
      if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
        document.querySelectorAll('.navmenu a.active').forEach(link => link.classList.remove('active'));
        navmenulink.classList.add('active');
      } else {
        navmenulink.classList.remove('active');
      }
    })
  }
  window.addEventListener('load', navmenuScrollspy);
  document.addEventListener('scroll', navmenuScrollspy);


/* 광고팝업에 관한 코드 */
// const myModal1 = new bootstrap.Modal(document.getElementById('modal1'), {
//   backdrop: false,  // 백드롭 비활성화 (모달 외부 클릭 가능)
//   scrollable: true  // 페이지 스크롤 허용
// });

const selectModals = document.querySelectorAll('.popupModal')

const myModals = [...selectModals].map( (element) =>
    new bootstrap.Modal(element, {
      backdrop: false,  // 백드롭 비활성화 (모달 외부 클릭 가능)
      scrollable: true  // 페이지 스크롤 허용
    })
)

myModals.forEach( (modal, idx) => {
  let value = document.cookie.match('(^|;) ?' + popups[idx].no + '=([^;]*)(;|$)');
  if (value == null) {
    modal.show();
    console.log("xkqdms: " + (Number(selectModals[idx!==0?idx-1:0].firstElementChild.style.top.split('px')[0]) + 300) + 'px')
    // selectModals[idx].firstElementChild.style.top = (Number(selectModals[idx!==0?idx-1:0].firstElementChild.style.top.split('px')[0]) + 300) + 'px';
    document.body.classList.remove('modal-open');
    document.body.style.overflow = null
    document.body.style.paddingRight = null
  }
})

// 모달 외부 클릭시 모달 꺼짐
// document.addEventListener('click', function (event) {
//
//   selectModals.forEach( (modal, idx) => {
//     let modalBox = modal.firstElementChild.firstElementChild
//
//     try {
//       let insideModal = modalBox.contains(event.target);
//       if (!insideModal && myModals[idx]._isShown) {
//         // myModal1.hide();
//         selectModals[idx].remove();
//       }
//     } catch (e) {
//       document.onclick = null;
//     }
//   })
//
// })

/* 광고팝업에 관한 코드 -끝- */
/**
   * calendar modal
   */
        // 모달 요소 가져오기
        var modal = document.getElementById("calendarModal");
        var btn = document.getElementById("openModal");
        var span = document.getElementsByClassName("close")[0];

        // 버튼 클릭 시 모달 열기
        btn.onclick = function() {
            modal.style.display = "block";
        }

        // X 버튼 클릭 시 모달 닫기
        span.onclick = function() {
            modal.style.display = "none";
        }

        // 모달 바깥 클릭 시 모달 닫기
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

})();

/*타이틀 타이핑 효과 구현*/

const $text = document.querySelector(".typing .text");

// 글자 모음
const letters = [
  "Website",
  "Network Security",
  "Game",
  "hacking"
];

// 글자 입력 속도
const speed = 100;
let i = 0;

// 타이핑 효과
const typing = async () => {
  const letter = letters[i].split("");

  while (letter.length) {
    await wait(speed);
    $text.innerHTML += letter.shift();
  }

  // 잠시 대기
  await wait(800);

  // 지우는 효과
  remove();
}

// 글자 지우는 효과
const remove = async () => {
  const letter = letters[i].split("");

  while (letter.length) {
    await wait(speed);

    letter.pop();
    $text.innerHTML = letter.join("");
  }

  // 다음 순서의 글자로 지정, 타이핑 함수 다시 실행
  i = !letters[i+1] ? 0 : i + 1;
  typing();
}

// 딜레이 기능 ( 마이크로초 )
function wait(ms) {
  return new Promise(res => setTimeout(res, ms))
}

// 초기 실행
setTimeout(typing, 1500);



const makeCookie = (e) => {
  const popupId = e.target.dataset.indexNumber;
  const expirationDate = new Date();
  // expirationDate.setDate(expirationDate.getDate() + 7); //쿠키 만료
  expirationDate.setSeconds(expirationDate.getSeconds() + 7);
  const path = "/"; // 설정된 경로 및 하위경로에서만 쿠키 접근이 가능합니다.
  const domain = "example.com"; //해당 도메인에서만 쿠키 접근이 가능합니다.
  const secure = false; //true 로 설정할 시 http2 로만 쿠키에 접근할 수 있습니다.
  document.cookie = `${popupId}=${popupId}; expires=${expirationDate.toUTCString()}; path=${path};`;
}