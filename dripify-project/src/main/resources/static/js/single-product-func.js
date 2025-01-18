// Carousel
const mainImage = document.getElementById('main-image');
const thumbnails = document.querySelectorAll('.thumbnail');
const prevButton = document.getElementById('prev');
const nextButton = document.getElementById('next');

let currentIndex = 0;

// Function to update the main image
function updateMainImage(index) {
  const activeThumbnail = document.querySelector('.thumbnail.active');
  if (activeThumbnail) activeThumbnail.classList.remove('active', 'border-gray-700');

  thumbnails[index].classList.add('active', 'border-gray-700');
  mainImage.src = thumbnails[index].src;
}

// Add click event to thumbnails
thumbnails.forEach((thumbnail, index) => {
  thumbnail.addEventListener('click', () => {
    currentIndex = index;
    updateMainImage(currentIndex);
  });
});

// Navigate with arrows
prevButton.addEventListener('click', () => {
  currentIndex = (currentIndex > 0) ? currentIndex - 1 : thumbnails.length - 1;
  updateMainImage(currentIndex);
});

nextButton.addEventListener('click', () => {
  currentIndex = (currentIndex < thumbnails.length - 1) ? currentIndex + 1 : 0;
  updateMainImage(currentIndex);
});


function toggleModal() {
  const modal = document.getElementById("modal");
  modal.classList.toggle("hidden");
}

const heart = document.getElementById('heart');
const outlinedHeart = document.getElementById('outlined-heart');
const filledHeart = document.getElementById('filled-heart');

heart.addEventListener('click', () => {
  outlinedHeart.classList.toggle('hidden');
  filledHeart.classList.toggle('hidden');
});
