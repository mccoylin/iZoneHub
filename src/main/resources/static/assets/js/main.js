/**
* Template Name: NiceRestaurant
* Template URL: https://bootstrapmade.com/nice-restaurant-bootstrap-template/
* Updated: Jun 06 2025 with Bootstrap v5.3.6
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

(function() {
  "use strict";

  /**
   * ===================================================================
   * iZoneHub - Custom Application Logic
   * ===================================================================
   */
  document.addEventListener('DOMContentLoaded', () => {

    // --- 1. 元素選擇器 (集中管理) ---
    const authModalLabel = document.getElementById('authModalLabel');
    const signInForm = document.getElementById('signInForm');
    const signUpForm = document.getElementById('signUpForm');
    const switchToSignUp = document.getElementById('switchToSignUp');
    const switchToSignIn = document.getElementById('switchToSignIn');
    const userGreeting = document.getElementById('userGreeting');
    const userNameSpan = document.getElementById('userName');
    const loginBtn = document.getElementById('loginBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const authAlert = document.getElementById('authAlert');

    // --- 2. 房間 Modal 相關邏輯 (已整合) ---
    const roomItems = document.querySelectorAll('.menu-item');
    const reserveBtn = document.getElementById('reserveBtn');

    // 2.1 處理點擊房間項目，填充 Modal 內容
    roomItems.forEach(item => {
        item.addEventListener('click', function() {
            const roomId = this.dataset.roomId;
            const roomName = this.dataset.roomName;
            const roomDesc = this.dataset.roomDesc;
            const roomImage = this.dataset.roomImage;

            console.log('[Room Modal] Populating modal for room ID:', roomId);

            document.getElementById('roomModalLabel').textContent = roomName;
            document.getElementById('roomDesc').textContent = roomDesc;
            document.getElementById('roomImage').src = roomImage;

            if (reserveBtn) {
                reserveBtn.dataset.roomId = roomId;
            }
        });
    });

    // 2.2 處理 Modal 中「立即預約」按鈕的點擊事件
    if (reserveBtn) {
        reserveBtn.addEventListener('click', function() {
            const roomId = this.dataset.roomId;
            if (roomId) {
                console.log(`Redirecting to booking page, Room ID: ${roomId}`);
                // 根據使用者登入狀態決定行為
                const isLoggedIn = !userGreeting.classList.contains('d-none');

                if (isLoggedIn) {
                    // 如果已登入，直接跳轉
                    window.location.href = `/booking/${roomId}`;
                } else {
                    // 如果未登入，彈出登入視窗
                    // 假設您的登入 Modal ID 是 'authModal'
                    const authModal = new bootstrap.Modal(document.getElementById('authModal'));
                    authModal.show();
                    // 隱藏房間 Modal
                    const roomModal = bootstrap.Modal.getInstance(document.getElementById('roomModal'));
                    roomModal.hide();
                }
            } else {
                console.error('Reserve button clicked, but no room ID was found.');
                alert('發生錯誤，請重新選擇房間。');
            }
        });
    }

    // --- 3. 認證與 UI 管理 ---

    // 3.1 UI 更新函式



window.showLoggedInState = function(name) {
    if (userNameSpan) userNameSpan.textContent = name;
    if (userGreeting) userGreeting.classList.remove('d-none');

    if (loginBtn) {
        loginBtn.classList.add('d-none');
        loginBtn.style.setProperty('display', 'none', 'important');
    }

    if (logoutBtn) {
        logoutBtn.classList.remove('d-none'); // 這行保險用
        logoutBtn.style.setProperty('display', 'block', 'important');
    }
};
window.showLoggedOutState = function() {
    if (userGreeting) userGreeting.classList.add('d-none');
    if (userNameSpan) userNameSpan.textContent = '';

    if (loginBtn) {
        loginBtn.classList.remove('d-none');
        loginBtn.style.setProperty('display', 'block', 'important');
    }

    if (logoutBtn) {
        logoutBtn.classList.add('d-none'); // 這行保險用
        logoutBtn.style.setProperty('display', 'none', 'important');
    }
};


    function showAlert(message) {
        if (authAlert) {
            authAlert.textContent = message;
            authAlert.classList.remove('d-none');
        } else {
            alert(message);
        }
    }

    function hideAlert() {
        if (authAlert) {
            authAlert.classList.add('d-none');
        }
    }

    // 3.2 事件監聽
    if (signInForm) {
        signInForm.addEventListener('input', hideAlert);
        signInForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            hideAlert();

    try {
    const emailInput = signInForm.querySelector('input[name="email"]');
    const passwordInput = signInForm.querySelector('input[name="password"]');

            const formData = new URLSearchParams();
            formData.append('email', emailInput.value.trim());
            formData.append('password', passwordInput.value);

            const response = await fetch('/api/login', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              credentials: 'include',
              body: formData
            });


         const result = await response.json();

                if (response.ok && result.user) {
                    // 登入成功
                    console.log('[Login Success]', result); //確認

                    localStorage.setItem('currentUser', JSON.stringify(result.user));
                    showLoggedInState(result.user.name);

                    const authModal = bootstrap.Modal.getInstance(document.getElementById('authModal'));
                    authModal.hide();
                    signInForm.reset();
                } else {
                    showAlert(result.message || '登入失敗，請檢查您的帳號密碼。');
                    const passwordInput = signInForm.querySelector('input[name="password"]');
                    if (passwordInput) passwordInput.value = '';
                }
            } catch (error) {
                console.error('Login request error:', error);
                showAlert('登入時發生網路錯誤，請稍後再試。');
            }
        });
    }

    if (signUpForm) {
        signUpForm.addEventListener('input', hideAlert);
        signUpForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            hideAlert();

            const formData = new FormData(signUpForm);
            const registerData = Object.fromEntries(formData.entries());

            if (registerData.password !== registerData.confirm_password) {
                showAlert('兩次輸入的密碼不一致。');
                return;
            }

            try {
                const response = await fetch('/api/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(registerData),
                });
                const result = await response.json();

                if (response.status === 201) { // 201 Created
                    alert('註冊成功！請使用您的新帳號登入。');
                    if (switchToSignIn) switchToSignIn.click();
                    signUpForm.reset();
                } else {
                    showAlert(result.message || '註冊失敗，請稍後再試。');
                }
            } catch (error) {
                console.error('Register request error:', error);
                showAlert('註冊時發生網路錯誤，請稍後再試。');
            }
        });
    }

    // [安全性強化] 登出按鈕事件
    if (logoutBtn) {
        logoutBtn.addEventListener('click', async (event) => {
            event.preventDefault();
            try {
                // 步驟 1: 呼叫後端 API，銷毀伺服器上的 Session (最重要的一步)
                const response = await fetch('/api/logout', { method: 'POST',credentials: 'include' });
                if (!response.ok) {
                    console.error('Server logout failed.');
                }
            } catch (error) {
                console.error('Error calling logout API:', error);
            } finally {
                // 步驟 2: 無論後端是否成功，都清理前端狀態
                localStorage.removeItem('currentUser');
                showLoggedOutState();
                window.location.href = '/'; // 導向回首頁，確保狀態完全刷新
            }
        });
    }

    // 3.3 Modal 表單切換
    if (switchToSignUp) {
        switchToSignUp.addEventListener('click', (e) => {
            e.preventDefault();
            hideAlert();
            signInForm.classList.add('d-none');
            signUpForm.classList.remove('d-none');
            authModalLabel.textContent = '註冊 iZoneHub';
        });
    }
    if (switchToSignIn) {
        switchToSignIn.addEventListener('click', (e) => {
            e.preventDefault();
            hideAlert();
            signUpForm.classList.add('d-none');
            signInForm.classList.remove('d-none');
            authModalLabel.textContent = '登入 iZoneHub';
        });
    }

    // --- 4. 頁面載入時檢查登入狀態 ---
    // 立即執行的非同步函式 (IIFE) 來處理初始認證檢查
    (async function checkAuthenticationStatus() {
        try {
            // 步驟 1: 呼叫後端 API 驗證 session 狀態
            const response = await fetch('/api/auth/status', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include' // 確保發送 cookie，這非常重要！
            });

            if (response.ok) {
                const result = await response.json();
                if (result.loggedIn && result.user) {
                    // 伺服器確認已登入
                    console.log('Server confirmed: User is logged in.', result.user.name);
                    localStorage.setItem('currentUser', JSON.stringify(result.user)); // 同步更新 localStorage
                    showLoggedInState(result.user.name);
                } else {
                    // 伺服器確認未登入
                    console.log('Server confirmed: User is NOT logged in.');
                    localStorage.removeItem('currentUser'); // 清理可能過期的 localStorage
                    showLoggedOutState();
                }
            } else {
                // API 呼叫失敗 (例如 401 Unauthorized, 500 Server Error)，也視為未登入
                console.error('Auth status check failed with status:', response.status);
                localStorage.removeItem('currentUser');
                showLoggedOutState();
            }
        } catch (error) {
            // 網路錯誤等，同樣視為未登入，確保 UI 正確
            console.error('Error checking authentication status:', error);
            window.showLoggedOutState();
        }
    })(); // 函式定義後立即執行


  /**
   * ===================================================================
   * Template's Default JavaScript
   * ===================================================================
   */

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
  if (scrollTop) {
    scrollTop.addEventListener('click', (e) => {
      e.preventDefault();
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    });
  }

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

});
})();