# -------------------------------
# Required Libraries
# -------------------------------

install.packages("nortest")   # For Lilliefors test
install.packages("moments")   # For skewness
library(nortest)
library(moments)

# -------------------------------
# Menu
# -------------------------------

# Define options
options <- c(
  "10 VUs - DELETE request",
  "100 VUs - DELETE request",
  "10 VUs - POST request",
  "100 VUs - POST request"
)

# Show menu
choice <- menu(options, title = "Select a load test configuration:")

# -------------------------------
# Load the Data
# -------------------------------

nonmodular <- switch(choice,
               read.csv("nonmodular-load-delete-10vus-treated.csv"),
               read.csv("nonmodular-load-delete-100vus-treated.csv"),
               read.csv("nonmodular-load-post-10vus-treated.csv"),
               read.csv("nonmodular-load-post-100vus-treated.csv")
)

modular <- switch(choice,
                     read.csv("modular-load-delete-10vus-treated.csv"),
                     read.csv("modular-load-delete-100vus-treated.csv"),
                     read.csv("modular-load-post-10vus-treated.csv"),
                     read.csv("modular-load-post-100vus-treated.csv")
)

microservices <- switch(choice,
                  read.csv("microservices-load-delete-10vus-treated.csv"),
                  read.csv("microservices-load-delete-100vus-treated.csv"),
                  read.csv("microservices-load-post-10vus-treated.csv"),
                  read.csv("microservices-load-post-100vus-treated.csv")
)

nm <- nonmodular$metric_value
m <- modular$metric_value
ms <- microservices$metric_value

# -------------------------------
# Normality Test (Lilliefors)
# -------------------------------

cat("\n--- Normality Test (Lilliefors) ---\n")
cat("NonModular:\n")
print(lillie.test(nm))

cat("Modular:\n")
print(lillie.test(m))

cat("Microservices:\n")
print(lillie.test(ms))

# -------------------------------
# Skewness (Symmetry Check)
# -------------------------------

cat("\n--- Skewness (Symmetry Check) ---\n")
cat("NonModular Skewness:", skewness(nm), "\n")
cat("Modular Skewness:", skewness(m), "\n")
cat("Microservices Skewness:", skewness(ms), "\n")

# -------------------------------
# Summary Stats
# -------------------------------

cat("\n--- Summary Statistics ---\n")
summary_stats <- function(data, label) {
  cat("\n", label, "\n")
  print(summary(data))
  cat("Standard Deviation:", sd(data), "\n")
}

summary_stats(nm, "NonModular")
summary_stats(m, "Modular")
summary_stats(ms, "Microservices")

# -------------------------------
# Hypothesis Testing
# -------------------------------

# Helper to decide and run the right test
run_tests <- function(group1, group2, label1, label2) {
  cat(paste0("\n--- Comparing ", label1, " vs ", label2, " ---\n"))
  
  normal1 <- lillie.test(group1)$p.value >= 0.05
  normal2 <- lillie.test(group2)$p.value >= 0.05
  
  if (normal1 && normal2) {
    result <- t.test(group1, group2)
    cat("Using t-test (both normal)\n")
  } else {
    result <- wilcox.test(group1, group2)
    cat("Using Wilcoxon test (non-normal data)\n")
  }
  
  print(result)
}

# Pairwise comparisons
run_tests(nm, m, "NonModular", "Modular")
run_tests(nm, ms, "NonModular", "Microservices")
run_tests(m, ms, "Modular", "Microservices")

