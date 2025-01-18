document.querySelectorAll('.toggle-btn').forEach(button => {
    button.addEventListener('click', () => {
      const content = button.nextElementSibling;
      console.log(content);

      content.classList.toggle('hidden');
    });
  });

  const filterMenu = document.getElementById("dropdown-filter");

  function toggleFilterMenu() {
    filterMenu.classList.toggle('opacity-0');
    filterMenu.classList.toggle('opacity-100');

    filterMenu.classList.toggle('pointer-events-none');
    filterMenu.classList.toggle('pointer-events-auto');
  }
