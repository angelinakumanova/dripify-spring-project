AOS.init();

const scrollToTopButton = document.getElementById('scrollToTop');
if (scrollToTopButton) {
  window.addEventListener('scroll', () => {
    if (window.scrollY > 1000) {
      scrollToTopButton.classList.add('opacity-100');
      scrollToTopButton.classList.remove('opacity-0');
    } else {
      scrollToTopButton.classList.add('opacity-0');
      scrollToTopButton.classList.remove('opacity-100');
    }
  });

  scrollToTopButton.addEventListener('click', () => {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  });
}

function toggleTabClasses(activeTab, inactiveTab, activeDiv, inactiveDiv) {
  activeTab.classList.replace('font-semibold', 'font-bold');
  activeTab.classList.add('border');
  activeDiv.classList.remove('hidden');

  inactiveTab.classList.replace('font-bold', 'font-semibold');
  inactiveTab.classList.remove('border');
  inactiveDiv.classList.add('hidden');
}

const purchasedBtn = document.getElementById('purchasedBtn');
const shippedBtn = document.getElementById('shippedBtn');
const purchasedOrders = document.getElementById('purchased-orders');
const shippedOrders = document.getElementById('shipped-orders');

if (purchasedBtn && shippedBtn && purchasedOrders && shippedOrders) {
  purchasedBtn.addEventListener('click', () => toggleTabClasses(purchasedBtn, shippedBtn, purchasedOrders, shippedOrders));
  shippedBtn.addEventListener('click', () => toggleTabClasses(shippedBtn, purchasedBtn, shippedOrders, purchasedOrders));
}

const itemsButton = document.getElementById('items-btn');
const reviewsButton = document.getElementById('reviews-btn');
const itemsSection = document.getElementById('items-section');
const reviewsSection = document.getElementById('reviews-section');

if (itemsButton && reviewsButton && itemsSection && reviewsSection) {
  itemsButton.addEventListener('click', () => toggleTabClasses(itemsButton, reviewsButton, itemsSection, reviewsSection));
  reviewsButton.addEventListener('click', () => toggleTabClasses(reviewsButton, itemsButton, reviewsSection, itemsSection));
}


const addToCartBtns = document.querySelectorAll('.add-to-cart-btn');
if (addToCartBtns) {
  addToCartBtns.forEach(b => {
    b.addEventListener('click', () => {
      b.classList.add('bg-pink-700', 'hover:bg-pink-700');
      b.classList.remove('bg-zinc-900', 'hover:bg-zinc-700');


      setTimeout(() => {
        b.classList.remove('bg-pink-700', 'hover:bg-pink-800');
        b.classList.add('bg-zinc-900', 'hover:bg-zinc-700')
      }, 2000);
    });
  });

  const removeButtons = document.querySelectorAll('.remove-btn');
  if (removeButtons) {
    removeButtons.forEach(b => {
      b.addEventListener('click', () => {
        b.closest('.item').remove();
      });
    });
  }

}



