# Loki LogQL - De 6

## 1. All logs
{job=~".+"}

## 2. Error logs
{job=~".+"} |~ "(?i)error|exception|failed"

## 3. Warning logs
{job=~".+"} |~ "(?i)warn|warning"
