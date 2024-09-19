# -*- coding: utf-8 -*-
"""
Created on Wed Mar 27 14:55:55 2024

@author: yashs
"""

import cv2

# Read the image containing the QR code
image = cv2.imread("static/QRs/Pratg3458903_QR.png")

# Create a QR code detector
qr_code_detector = cv2.QRCodeDetector()

# Detect and decode the QR code
decoded_data, points, _ = qr_code_detector.detectAndDecode(image)

print(decoded_data)
