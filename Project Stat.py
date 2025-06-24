import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression, LogisticRegression
from sklearn.metrics import mean_squared_error, classification_report, accuracy_score

data_path = "/content/tmdb_5000_movies.csv"
data = pd.read_csv(data_path)

print(data.head())
print(data.info())
print(data.describe())

data = data.dropna(subset=['budget', 'revenue', 'runtime'])

data['success'] = (data['revenue'] > 1e8).astype(int)

predictors = ['budget', 'runtime']
X = data[predictors]

y_linear = data['revenue']

y_logistic = data['success']

X_train, X_test, y_train_linear, y_test_linear = train_test_split(X, y_linear, test_size=0.2, random_state=42)
X_train_log, X_test_log, y_train_log, y_test_log = train_test_split(X, y_logistic, test_size=0.2, random_state=42)

linear_model = LinearRegression()
linear_model.fit(X_train, y_train_linear)
linear_preds = linear_model.predict(X_test)

print("Linear Regression RMSE:", np.sqrt(mean_squared_error(y_test_linear, linear_preds)))

logistic_model = LogisticRegression()
logistic_model.fit(X_train_log, y_train_log)
logistic_preds = logistic_model.predict(X_test_log)

print("Logistic Regression Accuracy:", accuracy_score(y_test_log, logistic_preds))
print(classification_report(y_test_log, logistic_preds))

X_test_log['predicted_success'] = logistic_preds
X_test_log['actual_success'] = y_test_log
X_test_log['title'] = data.loc[X_test_log.index, 'original_title']

print(X_test_log[['title', 'actual_success', 'predicted_success']])


plt.figure(figsize=(8, 6))
plt.scatter(y_test_linear, linear_preds, alpha=0.7)
plt.plot([0, max(y_test_linear)], [0, max(y_test_linear)], color='red', linestyle='--')
plt.title('Actual vs Predicted Revenue (Linear Regression)')
plt.xlabel('Actual Revenue')
plt.ylabel('Predicted Revenue')
plt.show()

plt.figure(figsize=(10, 8))

numeric_data = data.select_dtypes(include=np.number)
sns.heatmap(numeric_data.corr(), annot=True, cmap='coolwarm')
plt.title('Correlation Matrix')
plt.show()

