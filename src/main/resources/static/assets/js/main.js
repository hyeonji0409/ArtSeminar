/**
* Template Name: Arsha
* Template URL: https://bootstrapmade.com/arsha-free-bootstrap-html-template-corporate/
* Updated: Aug 07 2024 with Bootstrap v5.3.3
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

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
  const mobileNavToggleBtn = document.querySelector('.mobile-nav-toggle');

  function mobileNavToogle() {
    document.querySelector('body').classList.toggle('mobile-nav-active');
    mobileNavToggleBtn.classList.toggle('bi-list');
    mobileNavToggleBtn.classList.toggle('bi-x');
  }
  mobileNavToggleBtn.addEventListener('click', mobileNavToogle);

  /**
   * Hide mobile nav on same-page/hash links
   */
  document.querySelectorAll('#navmenu a').forEach(navmenu => {
    navmenu.addEventListener('click', () => {
      if (document.querySelector('.mobile-nav-active')) {
        mobileNavToogle();
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
   * login dropdowns - login, sign
   */

  function toggleDropdown() {
      const dropdownMenu = document.getElementById("dropdown-menu");
      dropdownMenu.classList.toggle("show");
  }

  // 드롭다운 외부 클릭 시 닫기
  window.onclick = function(event) {
      if (!event.target.matches('#login-button')) {
          const dropdowns = document.getElementById("dropdown-menu");
          if (dropdowns.classList.contains('show')) {
              dropdowns.classList.remove('show');
          }
      }
  }




  // /**
  //  * Preloader
  //  */
  // const preloader = document.querySelector('#preloader');
  // if (preloader) {
  //   window.addEventListener('load', () => {
  //     preloader.remove();
  //   });
  // }
  // preloader.remove();

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
   * Frequently Asked Questions Toggle
   */
  document.querySelectorAll('.faq-item h3, .faq-item .faq-toggle').forEach((faqItem) => {
    faqItem.addEventListener('click', () => {
      faqItem.parentNode.classList.toggle('faq-active');
    });
  });

  /**
   * Animate the skills items on reveal
   */
  let skillsAnimation = document.querySelectorAll('.skills-animation');
  skillsAnimation.forEach((item) => {
    new Waypoint({
      element: item,
      offset: '80%',
      handler: function(direction) {
        let progress = item.querySelectorAll('.progress .progress-bar');
        progress.forEach(el => {
          el.style.width = el.getAttribute('aria-valuenow') + '%';
        });
      }
    });
  });

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

})();

/**
 * Open blog link in new tab
 */
document.addEventListener('DOMContentLoaded', () => {
  const blogLink = document.querySelector('.btn-watch-video');

  if (blogLink) {
    // glightbox 클래스가 없을 경우에만 새 탭에서 열기
    blogLink.addEventListener('click', (e) => {
      e.preventDefault();


      if (!blogLink.classList.contains('glightbox')) {
        window.open(blogLink.href, '_blank');
      }
    });
  }
});

// 모달 및 캘린더 관련 코드
const modal = document.getElementById("calendarModal");
const closeModal = document.querySelector(".close");

document.getElementById("openModal").onclick = function() {
    modal.style.display = "block";

    // 캘린더 초기화
    $('#calendarModalContent').fullCalendar({
        // 캘린더 설정
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        },
        editable: true,
        events: [
            // 여기에 이벤트 추가
            {
                title: '이벤트 1',
                start: '2024-09-30'
            },
            {
                title: '이벤트 2',
                start: '2024-10-01',
                end: '2024-10-02'
            }
            // 추가 이벤트...
        ]
    });
};

// 모달 닫기
closeModal.onclick = function() {
    modal.style.display = "none";
};

// 모달 바깥 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};













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
/* 광고팝업에 관한 코드 -끝- */