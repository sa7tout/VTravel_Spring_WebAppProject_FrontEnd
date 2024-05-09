document.addEventListener("DOMContentLoaded", function() {
    const steps = document.querySelectorAll(".mdl-stepper-step");
    const tabs = document.querySelectorAll(".tab-pane");

    // Trigger click event on the tab associated with the first step
    document.querySelector('[data-target="#stepper-step-1"]').click();

    const nextButtons = document.querySelectorAll(".next-step");
    const prevButtons = document.querySelectorAll(".prev-step");

    nextButtons.forEach(button => {
        button.addEventListener("click", function() {
            const currentStep = this.closest(".tab-pane");

            // Check if the current step is Step 1
            if (currentStep.id === "stepper-step-1") {
                const inputs = currentStep.querySelectorAll("input[required]");
                let isValid = true;
                inputs.forEach(input => {
                    if (!input.value.trim()) {
                        isValid = false;
                        input.classList.add("is-invalid");
                    } else {
                        input.classList.remove("is-invalid");
                    }
                });

                // If all required fields are filled, proceed to the next step
                if (isValid) {
                    const nextStep = currentStep.nextElementSibling;
                    currentStep.classList.add("step-done");
                    currentStep.classList.remove("in", "active");
                    nextStep.classList.add("in", "active");

                    // Update stepper circles
                    const currentStepNumber = parseInt(currentStep.getAttribute("id").split("-")[2]);
                    const nextStepNumber = parseInt(nextStep.getAttribute("id").split("-")[2]);
                    const currentStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${currentStepNumber})`);
                    const nextStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${nextStepNumber})`);
                    currentStepCircle.classList.remove("active-step");
                    nextStepCircle.classList.add("active-step");
                }
            } else {
                // If not in Step 1, proceed to the next step directly
                const nextStep = currentStep.nextElementSibling;
                currentStep.classList.add("step-done");
                currentStep.classList.remove("in", "active");
                nextStep.classList.add("in", "active");

                // Update stepper circles
                const currentStepNumber = parseInt(currentStep.getAttribute("id").split("-")[2]);
                const nextStepNumber = parseInt(nextStep.getAttribute("id").split("-")[2]);
                const currentStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${currentStepNumber})`);
                const nextStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${nextStepNumber})`);
                currentStepCircle.classList.remove("active-step");
                nextStepCircle.classList.add("active-step");
            }
        });
    });

    prevButtons.forEach(button => {
        button.addEventListener("click", function() {
            const currentStep = this.closest(".tab-pane");
            const prevStep = currentStep.previousElementSibling;
            currentStep.classList.remove("in", "active");
            prevStep.classList.add("in", "active");

            // Update stepper circles
            const currentStepNumber = parseInt(currentStep.getAttribute("id").split("-")[2]);
            const prevStepNumber = parseInt(prevStep.getAttribute("id").split("-")[2]);
            const currentStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${currentStepNumber})`);
            const prevStepCircle = document.querySelector(`.mdl-stepper-step:nth-child(${prevStepNumber})`);
            currentStepCircle.classList.remove("active-step");
            prevStepCircle.classList.add("active-step");
        });
    });
});



    document.addEventListener("DOMContentLoaded", function() {
        const paymentOptions = document.querySelectorAll('input[type="checkbox"][name="payment_method"]');

        // Check card by default
        document.getElementById("card").checked = true;

        // Add event listener to payment checkboxes
        paymentOptions.forEach(option => {
            option.addEventListener("change", function() {
                // Uncheck other payment option if this one is checked
                if (this.checked) {
                    paymentOptions.forEach(otherOption => {
                        if (otherOption !== this) {
                            otherOption.checked = false;
                        }
                    });
                }
            });
        });
    });
