document.addEventListener('DOMContentLoaded', () => {
    const marqueeContainer = document.querySelector('.marquee-container');
    const images = marqueeContainer.querySelectorAll('img');

    // Function to calculate and set the marquee offset
    const setMarqueeOffset = () => {
      // Get the total width of one set of images
      const firstSet = Array.from(images).slice(0, images.length / 2);
      const totalWidth = firstSet.reduce((sum, img) => sum + img.offsetWidth, 0);

      // Get container width
      const containerWidth = marqueeContainer.offsetWidth;

      // Calculate gap offset in percentage (based on container width)
      const gapOffset = (totalWidth / containerWidth) * 100;

      // Apply percentage gap offset to the CSS variable
      marqueeContainer.style.setProperty('--marquee-offset', `-${gapOffset}%`);
    };

    // Run the function to set the offset when the page is loaded
    setMarqueeOffset();

    // Track the last zoom level
    let lastZoomLevel = window.devicePixelRatio;

    // Function to check for zoom changes
    const checkZoomChange = () => {
      const currentZoomLevel = window.devicePixelRatio;

      if (currentZoomLevel !== lastZoomLevel) {
        // Restart the marquee effect
        marqueeContainer.classList.remove('animate-marquee'); // Remove animation
        void marqueeContainer.offsetWidth; // Trigger reflow
        marqueeContainer.classList.add('animate-marquee'); // Reapply animation

        // Recalculate the offset after zoom change
        setMarqueeOffset();

        lastZoomLevel = currentZoomLevel; // Update zoom level
      }
    };

    // Listen for window resize (which includes zoom)
    window.addEventListener('resize', checkZoomChange);
  });
