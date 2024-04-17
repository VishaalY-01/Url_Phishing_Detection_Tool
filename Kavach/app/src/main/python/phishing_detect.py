import re

def predict(data, weight):
    f = sum(data[j] * weight[j] for j in range(len(data)))
    return 1 if f > 0 else -1

def is_ip_in_url(url):
    reg = r'\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}[\.]{1}\d{1,3}'
    if re.search(reg, url) is None:
        return -1
    else:
        return 1

def is_long_url(url):
    if len(url) < 54:
        return -1
    elif 54 <= len(url) <= 75:
        return 0
    else:
        return 1

def is_tiny_url(url):
    if len(url) > 20:
        return -1
    else:
        return 1

def is_alpha_numeric_url(url):
    if '@' not in url:
        return -1
    else:
        return 1

def is_redirecting_url(url):
    reg1 = re.compile('^http:')
    reg2 = re.compile('^https:')
    srch = '//'
    if url.find(srch) == 5 and reg1.search(url) and url[7:].find(srch) == -1:
        return -1
    elif url.find(srch) == 6 and reg2.search(url) and url[8:].find(srch) == -1:
        return -1
    else:
        return 1

def UrlPhishingDetector(url):
    testdata = [
        is_ip_in_url(url),
        is_long_url(url),
        is_tiny_url(url),
        is_alpha_numeric_url(url),
        is_redirecting_url(url),
        # Add other function calls with url parameter here
    ]

    prediction = predict(testdata, [3.33346292e-01, -1.11200396e-01, -7.77821806e-01, 1.11058590e-01, 3.89430647e-01,
                                    1.99992062e+00, 4.44366975e-01, -2.77951957e-01, -6.00531647e-05, 3.33200243e-01,
                                    2.66644002e+00, 6.66735991e-01, 5.55496098e-01, 5.57022408e-02, 2.22225591e-01,
                                    -1.66678858e-01])

    if prediction == -1:
        return "Not Phishing"
    elif prediction == 1:
        return "Phishing"
    else:
        return "Invalid Input"

# Example usage
if __name__ == "__main__":
    url_input = input("Enter the URL to check: ")
    result = UrlPhishingDetector(url_input)
    print("Prediction:", result)
