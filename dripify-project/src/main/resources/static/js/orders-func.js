const detailsButtons = document.querySelectorAll('.details-btn');
detailsButtons.forEach(b => {
  b.addEventListener('click', () => {
    const items = b.closest('.order').querySelector('.order-details');

    if (!items.classList.contains('order-details')) {
      console.warn('Incorrect element')
      return;
    }


    items.classList.toggle('hidden');
    if (items.classList.contains('hidden')) {
      b.textContent = 'View Details';
    } else {
      b.textContent = 'Close Details'
    }
  });
});


// TODO: MIGHT BE DIFFRENT AS DB FETCH MAY BE NEEDED
const statusButtons = document.querySelectorAll('.status-btn');
statusButtons.forEach(b => {
  b.addEventListener('click', () => {
    const parentElement = b.closest('.order');
    const statusElement = parentElement.querySelector('.status');

    if (b.textContent.includes('Shipped')) {
      statusElement.classList.replace('text-yellow-600', 'text-orange-600');
      statusElement.textContent = 'Shipped';
    } else {
      statusElement.classList.replace('text-orange-600', 'text-green-600');
      statusElement.textContent = 'Completed';

      // TODO: SAME AS IN ACCOUNT SETTINGS - MERGE THEM
      const formElement = parentElement.querySelector('.form-rating');
      const reviewCancelButton = formElement.querySelector('.cancel-rating');
      const reviewModal = formElement.querySelector('.review-modal');
      const reviewBackdrop = formElement.querySelector('.review-backdrop');
      const reviewPanel = formElement.querySelector('.review-panel');

      function toggleDeactivateModal() {
        reviewModal.classList.toggle('pointer-events-none');
        reviewBackdrop.classList.toggle('opacity-0');
        reviewBackdrop.classList.toggle('opacity-100');
        reviewPanel.classList.toggle('opacity-0');
        reviewPanel.classList.toggle('translate-y-4');
        reviewPanel.classList.toggle('translate-y-0');
        reviewPanel.classList.toggle('sm:scale-95');
        reviewPanel.classList.toggle('sm:scale-100');
      }

      function setupStarRating(section) {
        const stars = document.querySelectorAll(`.${section} .star`);
        stars.forEach((star) => {
          star.addEventListener('click', () => {
            const value = star.getAttribute('data-value');
            stars.forEach((s, index) => {
              s.textContent = index < value ? '⭐' : '☆';
            });
            star.parentElement.setAttribute('data-rating', value);
          });
        });
      }

      toggleDeactivateModal();
      reviewCancelButton.addEventListener('click', toggleDeactivateModal);
      setupStarRating('seller-rating');
      setupStarRating('order-rating');
    }

    b.remove();
  })
});
