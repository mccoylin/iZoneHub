/**
* Template Name: NiceRestaurant
* Template URL: https://bootstrapmade.com/nice-restaurant-bootstrap-template/
* Updated: Jun 06 2025 with Bootstrap v5.3.6
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

// ==========  自訂腳本開始 ==========

const authModalLabel = document.getElementById('authModalLabel');
const signInForm = document.getElementById('signInForm');
const signUpForm = document.getElementById('signUpForm');
const switchToSignUp = document.getElementById('switchToSignUp');
const switchToSignIn = document.getElementById('switchToSignIn');
const userGreeting = document.getElementById('userGreeting');
const userName = document.getElementById('userName');
const loginBtn = document.getElementById('loginBtn');
const logoutBtn = document.getElementById('logoutBtn');

// 表單切換
switchToSignUp.addEventListener('click', function(e){
    e.preventDefault();
    signInForm.classList.add('d-none');
    signUpForm.classList.remove('d-none');
    authModalLabel.textContent = '註冊 iZoneHub';
});

switchToSignIn.addEventListener('click', function(e){
    e.preventDefault();
    signUpForm.classList.add('d-none');
    signInForm.classList.remove('d-none');
    authModalLabel.textContent = '登入 iZoneHub';
});

// 註冊表單處理
signUpForm.addEventListener('submit', function(e) {
    e.preventDefault();
    console.log('[LOG] 註冊表單送出:', signUpForm);
    const formData = new FormData(signUpForm);
    const userData = {
        name: formData.get('name'),
        email: formData.get('email'),
        password: formData.get('password')
    };
    console.log('[LOG] 註冊資料:', userData);
    // 發送註冊請求到後台
    fetch('/api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        console.log('[LOG] 註冊 API 回應:', response);
        // 新增：將回應寫入 log
        response.clone().json().then(data => {
            console.log('[LOG] 後端回應內容:', data);
        }).catch(() => {
            response.clone().text().then(text => {
                console.log('[LOG] 後端回應文字:', text);
            });
        });
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // 註冊成功，更新使用者介面
            updateUserUI(data.user.name);

            // 關閉模態框
            const authModal = bootstrap.Modal.getInstance(document.getElementById('authModal'));
            authModal.hide();

            // 重置表單
            signUpForm.reset();

            // 顯示成功訊息
            alert('註冊成功！歡迎加入 iZoneHub');
        } else {
            // 顯示錯誤訊息
            alert(data.message || '註冊失敗，請稍後再試');
        }
    })
    .catch(error => {
        console.error('[LOG] 註冊 API 錯誤:', error);
        alert('註冊過程中發生錯誤，請稍後再試');
    });
});

// 登入表單處理
signInForm.addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(signInForm);
    const loginData = {
        email: formData.get('email'),
        password: formData.get('password')
    };

    // 發送登入請求到後台
    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 登入成功，更新使用者介面
            updateUserUI(data.user.name);

            // 關閉模態框
            const authModal = bootstrap.Modal.getInstance(document.getElementById('authModal'));
            authModal.hide();

            // 重置表單
            signInForm.reset();
        } else {
            // 顯示錯誤訊息
            alert(data.message || '登入失敗，請檢查您的帳號密碼');
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        alert('登入過程中發生錯誤，請稍後再試');
    });
});

// 更新使用者介面
function updateUserUI(name) {
    userName.textContent = name;
    userGreeting.classList.remove('d-none');
    loginBtn.style.display = 'none';
    logoutBtn.style.display = 'inline-block';

    // 儲存使用者資訊到 localStorage
    localStorage.setItem('currentUser', name);
}

// 登出功能
logoutBtn.addEventListener('click', function(e) {
    e.preventDefault();

    // 發送登出請求到後台
    fetch('/api/logout', {
        method: 'POST'
    })
    .then(() => {
        // 重置使用者介面
        userGreeting.classList.add('d-none');
        loginBtn.style.display = 'inline-block';
        logoutBtn.style.display = 'none';

        // 清除本地儲存
        localStorage.removeItem('currentUser');

        alert('已成功登出');
    })
    .catch(error => {
        console.error('Logout error:', error);
    });
});

// 頁面載入時檢查使用者狀態
document.addEventListener('DOMContentLoaded', function() {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        updateUserUI(savedUser);
    }
});

// ==========  自訂腳本結束 ==========


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
  if (mobileNavToggleBtn) {
    mobileNavToggleBtn.addEventListener('click', mobileNavToogle);
  }

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
   * Auth Modal switch login/sign up
   */
document.addEventListener('DOMContentLoaded', function() {
  const authModalLabel = document.getElementById('authModalLabel');
  const signInForm = document.getElementById('signInForm');
  const signUpForm = document.getElementById('signUpForm');
  const switchToSignUp = document.getElementById('switchToSignUp');
  const switchToSignIn = document.getElementById('switchToSignIn');

  if (switchToSignUp && switchToSignIn) {
    switchToSignUp.addEventListener('click', function(e){
      e.preventDefault();
      signInForm.classList.add('d-none');
      signUpForm.classList.remove('d-none');
      authModalLabel.textContent = '註冊 iZoneHub';
    });

    switchToSignIn.addEventListener('click', function(e){
      e.preventDefault();
      signUpForm.classList.add('d-none');
      signInForm.classList.remove('d-none');
      authModalLabel.textContent = '登入 iZoneHub';
    });
  }
});


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
   * Initiate Pure Counter
   */
  new PureCounter();

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
// Modal for Room Details
document.addEventListener('DOMContentLoaded', function() {
  const reserveBtn = document.getElementById('reserveBtn');
  let selectedRoom = {}; // 用來記錄當前選擇的房間

  document.querySelectorAll('.menu-item').forEach(item => {
    item.addEventListener('click', function() {
      const title = this.querySelector('h5').innerText;
      const desc = this.querySelector('p').innerText;
      const imgSrc = this.querySelector('img').src;
    

      selectedRoom = {
        name: title,
        desc: desc,
        img: imgSrc
      };

      document.getElementById('roomModalLabel').innerText = title;
      document.getElementById('roomDesc').innerText = desc;
      document.getElementById('roomImage').src = imgSrc;

      const roomModal = new bootstrap.Modal(document.getElementById('roomModal'));
      roomModal.show();
    });
  });

  reserveBtn.addEventListener('click', function() {
    const url = `booking.html?roomName=${encodeURIComponent(selectedRoom.name)}&roomImage=${encodeURIComponent(selectedRoom.img)}`;
    window.location.href = url;
  });
});


