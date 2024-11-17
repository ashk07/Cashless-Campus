# -*- coding: utf-8 -*-
"""
Created on Mon Apr  1 10:02:41 2024

@author: yashs
"""

import schedule
import time

def my_function():
    print("Function called at 10:05 AM")

# Schedule the function to be called every day at 12:30 PM
schedule.every().day.at("10:05").do(my_function)

# Run the scheduler continuously
while True:
    schedule.run_pending()
    time.sleep(1)  # Sleep for 1 second to avoid high CPU usage
