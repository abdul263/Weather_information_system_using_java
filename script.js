const inputBox = document.querySelector(".input-box");
const searchBtn = document.getElementById("searchBtn");
const weather_img = document.querySelector(".weather-img");
const temperature = document.querySelector(".temperature");
const description = document.querySelector(".description");
const humidity = document.getElementById("humidity");
const wind_speed = document.getElementById("wind-speed");

const location_not_found = document.querySelector(".location-not-found");
const weather_body = document.querySelector(".weather-body");

async function checkWeather(city) {
  const api_key = "45be36d4ba644267e3441091a4743ce3";
  const url = `https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=${api_key}`;

  try {
    const response = await fetch(url);
    const weather_data = await response.json();

    if (weather_data.cod === "404") {
      location_not_found.style.display = "flex";
      weather_body.style.display = "none";
      console.log("Error: City not found");
      return;
    }

    console.log("Weather data fetched successfully");
    location_not_found.style.display = "none";
    weather_body.style.display = "flex";

    temperature.innerHTML = `${Math.round(weather_data.main.temp)}°C`;
    description.innerHTML = `${weather_data.weather[0].description}`;
    humidity.innerHTML = `${weather_data.main.humidity}%`;
    wind_speed.innerHTML = `${weather_data.wind.speed} Km/H`;

    // Selecting weather icon based on API data
    switch (weather_data.weather[0].main) {
      case "Clouds":
        weather_img.src = "assets/Sun and cloud.webp";
        break;
      case "Clear":
        weather_img.src = "assets/Sun.png";
        break;
      case "Rain":
        weather_img.src = "assets/Rain.jpg";
        break;
      case "Mist":
        weather_img.src = "assets/mist.jpg";
        break;
      case "Snow":
        weather_img.src = "assets/snow.jpg";
        break;
      case "Haze":
        weather_img.src = "assets/Haze.png";
        break;
      case "Smoke":
        weather_img.src = "assets/Smoke.jpg";
        break;
      default:
        weather_img.src = "assets/default.png";
    }
  } catch (error) {
    console.error("Error fetching weather data:", error);
  }
}

searchBtn.addEventListener("click", () => {
  checkWeather(inputBox.value);
});
