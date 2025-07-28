/**
* Template Name: NiceRestaurant
* Template URL: https://bootstrapmade.com/nice-restaurant-bootstrap-template/
* Updated: Jun 06 2025 with Bootstrap v5.3.6
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

// ==========  自訂腳本開始 ==========

document.addEventListener('DOMContentLoaded', function() {
    // Get room items
    const roomItems = document.querySelectorAll('.menu-item');
    console.log('[Room Modal] 頁面載入完成，找到', roomItems.length, '個房間項目。');

    // Add click event to each room item
    roomItems.forEach(item => {
        item.addEventListener('click', function() {
            console.log('[Room Modal] 使用者點擊了一個房間項目:', this);

            // 從 data-* 屬性中獲取資料
            const roomId = this.dataset.roomId;
            const roomName = this.dataset.roomName;
            const roomDesc = this.dataset.roomDesc;
            const roomPrice = this.dataset.roomPrice;
            const roomImage = this.dataset.roomImage;

            // 輸出獲取到的資料，方便除錯
            console.log('[Room Modal] 取得的房間資料:', {
                ID: roomId,
                名稱: roomName,
                描述: roomDesc,
                價格: roomPrice,
                圖片路徑: roomImage
            });

            // Set modal content
            console.log('[Room Modal] 正在更新 Modal 視窗的內容...');
            document.getElementById('roomModalLabel').textContent = roomName;
            document.getElementById('roomDesc').textContent = roomDesc;
            document.getElementById('roomImage').src = roomImage;

            // Set reserve button data if needed
            const reserveBtn = document.getElementById('reserveBtn');
            if (reserveBtn) {
                reserveBtn.dataset.roomId = roomId;
            }

            console.log('[Room Modal] Modal 內容更新完畢。');
        });
    });
});
// 這段程式碼應該用來處理您 Modal 視窗中「立即預約」按鈕的點擊事件

document.addEventListener('DOMContentLoaded', function() {
    // 這部分程式碼負責在使用者點擊某個房間時，將其 ID 暫存到「立即預約」按鈕上
    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', function() {
            // 從被點擊的房間項目中讀取 data-room-id
            const roomId = this.dataset.roomId;
            const reserveBtn = document.getElementById('reserveBtn');
            if (reserveBtn) {
                // 將 ID 存到按鈕的 dataset 中，以便後續使用
                reserveBtn.dataset.roomId = roomId;
            }
            // ... 您其他用來填充 Modal 內容的程式碼 ...
        });
    });

    // 這部分程式碼負責處理「立即預約」按鈕本身的點擊事件
    const reserveBtn = document.getElementById('reserveBtn');
    if (reserveBtn) {
        reserveBtn.addEventListener('click', function() {
            // 從按鈕的 data-* 屬性中讀取我們剛剛存入的 roomId
            const roomId = this.dataset.roomId;

            if (roomId) {
                console.log(`準備跳轉至預約頁面，房間 ID: ${roomId}`);

                // 建構正確的 URL 並進行頁面跳轉
                // 這將會產生一個像 "/booking/5" 這樣的 URL
                window.location.href = `/booking/${roomId}`;
            } else {
                console.error('「立即預約」按鈕被點擊，但找不到房間 ID。');
                alert('發生錯誤，請重新選擇房間。');
            }
        });
    }
});


/**
 * ===================================================================
 * iZoneHub - Authentication & UI Management (Refactored & Final)
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
    const passwordInput = signInForm.querySelector('input[name="password"]');
    const loginBtn = document.getElementById('loginBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const authAlert = document.getElementById('authAlert'); // 假設你的 Modal 中有這個 div 用於顯示錯誤

    // --- 2. UI 更新函式 ---

    // 更新 UI 為「已登入」狀態
    function showLoggedInState(name) {
        userNameSpan.textContent = name;
        userGreeting.classList.remove('d-none');

        // 隱藏登入按鈕，顯示登出按鈕
        loginBtn.classList.add('d-none');
        logoutBtn.classList.remove('d-none');
    }

    // 重設 UI 為「已登出」狀態
    function showLoggedOutState() {
        userGreeting.classList.add('d-none');
        userNameSpan.textContent = '';

        // 顯示登入按鈕，隱藏登出按鈕
        loginBtn.classList.remove('d-none');
        logoutBtn.classList.add('d-none');
    }

    // --- 3. 事件監聽 ---

    // 監聽登入表單
    if (signInForm) {
        // 當使用者重新輸入時，自動隱藏錯誤訊息
        signInForm.addEventListener('input', () => {
            if(authAlert) {
                authAlert.classList.add('d-none');
            }
        });

        signInForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if(authAlert) {
                authAlert.classList.add('d-none');
            }

            const formData = new FormData(signInForm);
            const loginData = Object.fromEntries(formData.entries());

            try {
                const response = await fetch('/api/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(loginData),
                });
                const result = await response.json();

                if (response.ok && result.userName) {
                    // 登入成功
                    localStorage.setItem('currentUser', result.userName);
                    showLoggedInState(result.userName);

                    const authModal = bootstrap.Modal.getInstance(document.getElementById('authModal'));
                    authModal.hide();
                    signInForm.reset();
                } else {
                    // 登入失敗
                    if(authAlert) {
                        authAlert.textContent = result.message || '登入失敗，請檢查您的帳號密碼。';
                        authAlert.classList.remove('d-none');
                        if (passwordInput) {
                            passwordInput.value = ''; // 清空密碼欄位
                        }
                    } else {
                        alert(result.message || '登入失敗，請檢查您的帳號密碼。');
                    }
                }
            } catch (error) {
                console.error('Login request error:', error);
                if(authAlert) {
                    authAlert.textContent = '登入時發生網路錯誤，請稍後再試。';
                    authAlert.classList.remove('d-none');
                } else {
                    alert('登入時發生網路錯誤，請稍後再試。');
                }
            }
        });
    }

    // 監聽註冊表單
    if (signUpForm) {
        // 當使用者重新輸入時，自動隱藏錯誤訊息
        signUpForm.addEventListener('input', () => {
            if(authAlert) {
                authAlert.classList.add('d-none');
            }
        });

        signUpForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if(authAlert) {
                authAlert.classList.add('d-none');
            }

            const formData = new FormData(signUpForm);
            const registerData = Object.fromEntries(formData.entries());

            if (registerData.password !== registerData.confirm_password) {
                if(authAlert) {
                    authAlert.textContent = '兩次輸入的密碼不一致。';
                    authAlert.classList.remove('d-none');
                } else {
                    alert('兩次輸入的密碼不一致。');
                }
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
                    switchToSignIn.click(); // 自動切換回登入表單
                    signUpForm.reset();
                } else {
                    if(authAlert) {
                        authAlert.textContent = result.message || '註冊失敗，請稍後再試。';
                        authAlert.classList.remove('d-none');
                    } else {
                        alert(result.message || '註冊失敗，請稍後再試。');
                    }
                }
            } catch (error) {
                console.error('Register request error:', error);
                if(authAlert) {
                    authAlert.textContent = '註冊時發生網路錯誤，請稍後再試。';
                    authAlert.classList.remove('d-none');
                } else {
                    alert('註冊時發生網路錯誤，請稍後再試。');
                }
            }
        });
    }

    // 監聽登出按鈕
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (event) => {
            event.preventDefault();
            localStorage.removeItem('currentUser');
            showLoggedOutState();
            // 可選：你可以在此處呼叫後端的 /api/logout 來清除伺服器 session
        });
    }

    // 監聽 Modal 中的表單切換連結
    if (switchToSignUp) {
        switchToSignUp.addEventListener('click', (e) => {
            e.preventDefault();
            if(authAlert) {
                authAlert.classList.add('d-none');
            }
            signInForm.classList.add('d-none');
            signUpForm.classList.remove('d-none');
            authModalLabel.textContent = '註冊 iZoneHub';
        });
    }
    if (switchToSignIn) {
        switchToSignIn.addEventListener('click', (e) => {
            e.preventDefault();
            if(authAlert) {
                authAlert.classList.add('d-none');
            }
            signUpForm.classList.add('d-none');
            signInForm.classList.remove('d-none');
            authModalLabel.textContent = '登入 iZoneHub';
        });
    }

    // --- 4. 頁面載入時檢查登入狀態 ---
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        showLoggedInState(savedUser);
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
//   /**
//    * Auth Modal switch login/sign up
//    */
// document.addEventListener('DOMContentLoaded', function() {
//   const authModalLabel = document.getElementById('authModalLabel');
//   const signInForm = document.getElementById('signInForm');
//   const signUpForm = document.getElementById('signUpForm');
//   const switchToSignUp = document.getElementById('switchToSignUp');
//   const switchToSignIn = document.getElementById('switchToSignIn');
//
//   if (switchToSignUp && switchToSignIn) {
//     switchToSignUp.addEventListener('click', function(e){
//       e.preventDefault();
//       signInForm.classList.add('d-none');
//       signUpForm.classList.remove('d-none');
//       authModalLabel.textContent = '註冊 iZoneHub';
//     });
//
//     switchToSignIn.addEventListener('click', function(e){
//       e.preventDefault();
//       signUpForm.classList.add('d-none');
//       signInForm.classList.remove('d-none');
//       authModalLabel.textContent = '登入 iZoneHub';
//     });
//   }
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
