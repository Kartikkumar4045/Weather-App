import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WeatherForecastApp {

    private static JFrame frame;
    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static JLabel weatherImageLabel; 
    private static JButton fetchButton;
    private static String apiKey = "853d8e00939d69cf66710564e80f7422";
    private static String imageFolderPath = "D:\\javacodes\\GUI Programing\\WeatherApp\\"; 
    private static String fetchWeatherData(String city) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();

            
            String temperatureString = "\"temp\":";
            String humidityString = "\"humidity\":";
            String descriptionString = "\"description\":\"";
            String windSpeedString = "\"speed\":";

            double temperatureKelvin = Double.parseDouble(jsonResponse.split(temperatureString)[1].split(",")[0]);
            double temperatureCelsius = temperatureKelvin - 273.15;

            int humidity = Integer.parseInt(jsonResponse.split(humidityString)[1].split(",")[0]);

            String description = jsonResponse.split(descriptionString)[1].split("\"")[0];

            double windSpeed = Double.parseDouble(jsonResponse.split(windSpeedString)[1].split(",")[0]);

            
            displayWeatherImage(description);

            return "DESCRIPTION: " + description + "\nTEMPERATURE: " + String.format("%.2f", temperatureCelsius) + " Celsius\nHUMIDITY: " + humidity + "%\nWIND SPEED: " + windSpeed + " m/s";
        } catch (Exception e) {
            return "Failed to fetch weather data. Please check your city and API key.";
        }
    }

    private static void displayWeatherImage(String description) {
        ImageIcon icon = null;
        switch (description.toLowerCase()) {
            case "clear sky":
            case "haze":
                icon = new ImageIcon(imageFolderPath + "clear_sky.png");
                break;
            case "few clouds":
            case "scattered clouds":
            case "broken clouds":
            case "overcast clouds":
                icon = new ImageIcon(imageFolderPath + "cloudy.png");
                break;
            case "light rain":
            case "moderate rain":
            case "heavy intensity rain":
            case "very heavy rain":
                icon = new ImageIcon(imageFolderPath + "rainy.png");
                break;
        
            case "shower rain":
            case "light intensity shower rain":
            case "heavy intensity shower rain":
                icon = new ImageIcon(imageFolderPath + "showers.png");
                break;
            case "thunderstorm":
            case "thunderstorm with light rain":
            case "thunderstorm with rain":
            case "thunderstorm with heavy rain":
                icon = new ImageIcon(imageFolderPath + "thunderstorm.png");
                break;
            case "snow":
            case "light snow":
            case "heavy snow":
                icon = new ImageIcon(imageFolderPath + "snow.png");
                break;
            default:
                
                icon = new ImageIcon(imageFolderPath + "default.png");
                break;
        }
        
        
        if (icon != null) {
            Image image = icon.getImage();  
            Image newimg = image.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH); 
            weatherImageLabel.setIcon(new ImageIcon(newimg));
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Weather Forecast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); 
        frame.setLayout(new FlowLayout());

        JLabel locationLabel = new JLabel("Enter city name:");
        locationLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        locationField = new JTextField(15);
        fetchButton = new JButton("Fetch Weather");
        weatherDisplay = new JTextArea(10, 30);
        weatherDisplay.setEditable(false);

    
        weatherImageLabel = new JLabel();
        weatherImageLabel.setPreferredSize(new Dimension(150, 150)); 

        
        Color skyBlue = new Color(135, 206, 235); 
        Color darkerSkyBlue = new Color(110, 180, 210); 
        frame.getContentPane().setBackground(skyBlue);
        locationField.setBackground(Color.LIGHT_GRAY);
        weatherDisplay.setBackground(darkerSkyBlue); 

        
        weatherDisplay.setBorder(new LineBorder(Color.BLACK, 1));

        
        locationField.setBorder(new LineBorder(Color.BLACK, 1));

        frame.add(locationLabel);
        frame.add(locationField);
        frame.add(fetchButton);
        frame.add(weatherDisplay);
        frame.add(weatherImageLabel); 

        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = locationField.getText();
                String weatherInfo = fetchWeatherData(city);
                weatherDisplay.setText(weatherInfo);
            }
        });

        frame.setVisible(true);
    }
}
