function bindToggleAnimation(trigger, target) {
  const triggerElement = document.getElementById(trigger);
  const targetElement = document.getElementById(target);

  if (!triggerElement || !targetElement) {
    console.warn(`Missing element: ${trigger || target}`);
    return;
  }

  triggerElement.addEventListener('click', () => toggleOpacityAnimation(targetElement));
  targetElement.addEventListener('mouseleave', () => toggleOpacityAnimation(targetElement));
}

bindToggleAnimation('flyout-menu-button', 'flyout-menu');
bindToggleAnimation('profile-name', 'profile-dropdown');


function toggleOpacityAnimation(element) {
  element.classList.toggle('opacity-0');
  element.classList.toggle('opacity-100');

  element.classList.toggle('pointer-events-none');
  element.classList.toggle('pointer-events-auto');
}


// Utility Functions
const toggleVisibility = (element) => {
  element.classList.toggle('hidden');
};

const toggleClasses = (element, classesToAdd, classesToRemove) => {
  element.classList.add(...classesToAdd);
  element.classList.remove(...classesToRemove);
};

// MOBILE MENU FUNCTIONALITY
const mobileMenuButton = document.querySelector('.mobile-menu button.hamburger');
const mobileMenu = document.querySelector('div[role="dialog"]');
const closeButton = mobileMenu.querySelector('button');
const backdrop = mobileMenu.querySelector('.fixed');

const toggleMenu = () => {
  toggleVisibility(mobileMenu);
  toggleVisibility(backdrop);
};

mobileMenuButton.addEventListener('click', toggleMenu);
closeButton.addEventListener('click', toggleMenu);
backdrop.addEventListener('click', toggleMenu);

// SHOP BUTTON FUNCTIONALITY
const mobileCloseButton = document.getElementById("mobile-close-button");
const mobileMenuShopButton = document.getElementById("shop-mobile-button");
const mobileReturnButton = document.getElementById("mobile-return-button");
const mobileNav = document.getElementById("mobile-nav");
const mobileCategoryMenu = document.getElementById("mobile-categories");

const toggleMenuViewCategory = () => {
  toggleVisibility(mobileNav);
  toggleVisibility(mobileCategoryMenu);
  toggleVisibility(mobileCloseButton);
  toggleVisibility(mobileReturnButton);
};

mobileMenuShopButton.addEventListener('click', () => toggleMenuViewCategory());
mobileReturnButton.addEventListener('click', () => toggleMenuViewCategory());


// CATEGORY VISIBILITY FUNCTIONALITY
const womenTab = document.getElementById('women-tab');
const menTab = document.getElementById('men-tab');

const womenTabMobile = document.getElementById('women-tab-mobile');
const menTabMobile = document.getElementById('men-tab-mobile');

womenTab.addEventListener('click', () => toggleCategoryVisibility(womenTab));
menTab.addEventListener('click', () => toggleCategoryVisibility(menTab));

womenTabMobile.addEventListener('click', () => toggleCategoryVisibility(womenTabMobile));
menTabMobile.addEventListener('click', () => toggleCategoryVisibility(menTabMobile));

const toggleCategoryVisibility = (current) => {
  const { categories: womenCategories, tab: womenTab, underline: womenUnderline } = getCategoryElements('women', current.id.includes('mobile'));
  const { categories: menCategories, tab: menTab, underline: menUnderline } = getCategoryElements('men', current.id.includes('mobile'));

  if (current.getAttribute('category') === 'women') {
    toggleVisibility(womenCategories);
    toggleVisibility(menCategories);
    toggleClasses(womenTab, ['font-bold'], ['font-semibold']);
    toggleClasses(menTab, ['font-semibold'], ['font-bold']);
    toggleClasses(womenUnderline, ['w-full'], []);
    toggleClasses(menUnderline, [], ['w-full']);
  } else if (current.getAttribute('category') === 'men') {
    toggleVisibility(menCategories);
    toggleVisibility(womenCategories);
    toggleClasses(menTab, ['font-bold'], ['font-semibold']);
    toggleClasses(womenTab, ['font-semibold'], ['font-bold']);
    toggleClasses(menUnderline, ['w-full'], ['w-0']);
    toggleClasses(womenUnderline, ['w-0'], ['w-full']);
  }
};

function getCategoryElements(category, isMobile = false) {
  const womenCategories = document.getElementById(isMobile ? 'women-categories-mobile' : 'women-categories');
  const menCategories = document.getElementById(isMobile ? 'men-categories-mobile' : 'men-categories');
  const womenTab = document.getElementById(isMobile ? 'women-tab-mobile' : 'women-tab');
  const menTab = document.getElementById(isMobile ? 'men-tab-mobile' : 'men-tab');

  if (category === 'women')
    return {
      categories: womenCategories,
      tab: womenTab,
      underline: womenTab.children[0],
    }

  return {
    categories: menCategories,
    tab: menTab,
    underline: menTab.children[0],
  }
};





