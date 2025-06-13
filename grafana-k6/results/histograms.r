# -------------------------------
# Required Libraries
# -------------------------------

library(ggplot2)

# -------------------------------
# CSV File Paths
# -------------------------------

file_names <- c(
  "nonmodular-load-delete-10vus-treated.csv",
  "nonmodular-load-delete-100vus-treated.csv",
  "nonmodular-load-post-10vus-treated.csv",
  "nonmodular-load-post-100vus-treated.csv",
  "modular-load-delete-10vus-treated.csv",
  "modular-load-delete-100vus-treated.csv",
  "modular-load-post-10vus-treated.csv",
  "modular-load-post-100vus-treated.csv",
  "microservices-load-delete-10vus-treated.csv",
  "microservices-load-delete-100vus-treated.csv",
  "microservices-load-post-10vus-treated.csv",
  "microservices-load-post-100vus-treated.csv"
)

titles <- c(
  "Non-Modular Monolithic - DELETE Request - 10 Concurrent Users",
  "Non-Modular Monolithic - DELETE Request - 100 Concurrent Users",
  "Non-Modular Monolithic - POST Request - 10 Concurrent Users",
  "Non-Modular Monolithic - POST Request - 100 Concurrent Users",
  "Modular Monotlihtic - DELETE Request - 10 Concurrent Users",
  "Modular Monotlihtic - DELETE Request - 100 Concurrent Users",
  "Modular Monotlihtic - POST Request - 10 Concurrent Users",
  "Modular Monotlihtic - POST Request - 100 Concurrent Users",
  "Microservices - DELETE Request - 10 Concurrent Users",
  "Microservices - DELETE Request - 100 Concurrent Users",
  "Microservices - POST Request - 10 Concurrent Users",
  "Microservices - POST Request- 100 Concurrent Users"
)

files <- setNames(file_names, titles)

# -------------------------------
# Histogram Generation Function
# -------------------------------

create_histogram <- function(file_path,title) {
  data <- read.csv(file_path)
  filename <- basename(file_path)
  
  ggplot(data, aes(x=metric_value)) +
    geom_histogram(binwidth=diff(range(data$metric_value))/30, fill="skyblue", color="black") +
    labs(title= title,
         x="Response Time (ms)",
         y="Frequency") +
    theme_minimal()
}

# -------------------------------
# Generate and Display Histograms
# -------------------------------

for (title in names(files)) {
  file_path <- files[[title]]
  print(create_histogram(file_path, title))
}
