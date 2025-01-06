 // MOBILE MENU FUNCTIONALITY
    // Get the elements
    const mobileMenuButton = document.querySelector('.mobile-menu button.hamburger'); // Hamburger button
    const mobileMenu = document.querySelector('div[role="dialog"]'); // Mobile menu

    const closeButton = mobileMenu.querySelector('button'); // Close button inside mobile menu
    const backdrop = mobileMenu.querySelector('.fixed'); // The backdrop to close when clicked


    // Function to open the menu
    const openMenu = () => {
      mobileMenu.classList.remove('hidden'); // Show the mobile menu
      backdrop.classList.remove('hidden'); // Show the backdrop
    };

    // Function to close the menu
    const closeMenu = () => {
      mobileMenu.classList.add('hidden'); // Hide the mobile menu
      backdrop.classList.add('hidden'); // Hide the backdrop
    };

    // Event listener to open the menu
    mobileMenuButton.addEventListener('click', openMenu);

    // Event listener to close the menu
    closeButton.addEventListener('click', closeMenu);
    backdrop.addEventListener('click', closeMenu);



    // SHOP BUTTON FUNCTIONALITY 
    const mobileCloseButton = document.getElementById("mobile-close-button");
    const mobileMenuShopButton = document.getElementById("shop-mobile-button");
    const mobileReturnButton = document.getElementById("mobile-return-button");
    const mobileNav = document.getElementById("mobile-nav");
    const mobileCategoryMenu = document.getElementById("mobile-categories");

    function toggleMenu() {
      mobileNav.classList.toggle('hidden');
      mobileCategoryMenu.classList.toggle('hidden');

      mobileCloseButton.classList.toggle('hidden');
      mobileReturnButton.classList.toggle('hidden');
    }

    function showCategoryMobile(category) {
      const womenCategories = document.getElementById('women-categories-mobile');
      const menCategories = document.getElementById('men-categories-mobile');

      const womenTab = document.getElementById('women-tab-mobile');
      const menTab = document.getElementById('men-tab-mobile');


      toggleCategoryVisibility(category, womenCategories, menCategories, womenTab, menTab)
    }



// LARGER SCREENS
    function showCategory(category) {
      // Get category elements
      const womenCategories = document.getElementById('women-categories');
      const menCategories = document.getElementById('men-categories');

      const womenTab = document.getElementById('women-tab');
      const menTab = document.getElementById('men-tab');


      toggleCategoryVisibility(category, womenCategories, menCategories, womenTab, menTab)
    }

    function toggleCategoryVisibility(category, womenCategories, menCategories, womenTab, menTab) {
      const womenTabUnderline = womenTab.children[0];
      const menTabUnderline = menTab.children[0];

      if (category === 'women') {
        // Show women's categories and update tab styles
        womenCategories.classList.remove('hidden');
        menCategories.classList.add('hidden');

        womenTab.classList.add('font-bold');
        womenTab.classList.remove('font-semibold');
        menTab.classList.replace('font-bold', 'font-semibold');

        womenTabUnderline.classList.add('w-full');
        menTabUnderline.classList.replace('w-full', 'w-0');
      } else if (category === 'men') {
        // Show men's categories and update tab styles  
        menCategories.classList.remove('hidden');
        womenCategories.classList.add('hidden');

        menTab.classList.add('font-bold');
        menTab.classList.remove('font-semibold');
        womenTab.classList.replace('font-bold', 'font-semibold');

        menTabUnderline.classList.add('w-full');
        womenTabUnderline.classList.replace('w-full', 'w-0');
      }
    }


    function toggleOpacityAnimation(element) {
      element.classList.toggle('opacity-0');
      element.classList.toggle('opacity-100');

      element.classList.toggle('pointer-events-none');
      element.classList.toggle('pointer-events-auto');
    }

    
