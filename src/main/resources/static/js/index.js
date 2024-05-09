document.addEventListener("DOMContentLoaded", function () {
    // Fetch destinations and images from API
    fetch('http://localhost:8080/api/destinations')
        .then(response => response.json())
        .then(data => {
            const destinationNames = data.map(destination => destination.destinationName);

            const destinationSelect = document.getElementById('destinationSelect');
            const destinationImages = document.getElementById('destinationImages');
            data.forEach((destination, index) => {
                // Add destination to dropdown
                const option = document.createElement('option');
                option.value = destination.destinationId;
                option.textContent = destination.destinationName;
                destinationSelect.appendChild(option);

                // Add image to slideshow
                const img = document.createElement('img');
                img.src = destination.imageURL;
                img.alt = destination.destinationName;
                img.classList.add('img-fluid');
                if (index === 0) {
                                    img.classList.add('active');
                                }

                img.dataset.id = index + 1; // Assign unique data-id attribute
                destinationImages.appendChild(img);

            });
            initializeTyped(destinationNames);
        })
        .catch(error => console.error('Error fetching destinations:', error));
});

function initializeTyped(destinationNames) {
    var slides = $('.slides'),
        images = slides.find('img');

    var typed = new Typed('.typed-words', {
        strings: destinationNames, // Use destinationNames array as strings
        typeSpeed: 80,
        backSpeed: 80,
        backDelay: 4000,
        startDelay: 1000,
        loop: true,
        showCursor: true,
        preStringTyped: (arrayPos, self) => {
            arrayPos++;
            images.removeClass('active'); // Remove active class from all images
            images.filter('[data-id="' + arrayPos + '"]').addClass('active'); // Add active class to the corresponding image
        }
    });
}
function validateForm() {
    var form = document.getElementsByClassName('form')[0];
    if (form.checkValidity() === false) {
        event.preventDefault();
        event.stopPropagation();
        form.classList.add('was-validated');
        return false;
    }
    form.classList.add('was-validated');
    return true;
}

