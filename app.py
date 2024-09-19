from flask import Flask
from flask import request
import pymysql
from werkzeug.utils import secure_filename
import os
import json
import qrcode
import cv2
from datetime import datetime 

import smtplib 
from email.mime.multipart import MIMEMultipart 
from email.mime.text import MIMEText
from email.mime.base import MIMEBase 
from email import encoders 
import schedule
import time 
import threading

app = Flask(__name__)

UPLOAD_FOLDER = 'static/schedule/'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

UPLOAD_FOLDER1 = 'static/imgvideo/'
app.config['UPLOAD_FOLDER1'] = UPLOAD_FOLDER1

app.secret_key = 'any random string' 

def dbConnection():
    try:
        connection = pymysql.connect(host="localhost", user="root", password="123456", database="cashlesscampus")
        return connection
    except:
        print("Something went wrong in database Connection")

def dbClose():
    try:
        dbConnection().close()
    except:
        print("Something went wrong in Close DB Connection")

con = dbConnection()
cursor = con.cursor()

adminmail = "vu1f2021055@pvppcoe.ac.in"

def sendemailtouser(usermail,ogpass):   
    fromaddr = "rockmike09@gmail.com"
    toaddr = usermail
    msg = MIMEMultipart()   
    msg['From'] = fromaddr  
    msg['To'] = toaddr 
    msg['Subject'] = "Cashless Campass"
    body = ogpass
    msg.attach(MIMEText(body, 'plain'))  
    s = smtplib.SMTP('smtp.gmail.com', 587) 
    s.starttls() 
    s.login(fromaddr, "ioptfwsacxmodyjs") 
    text = msg.as_string() 
    s.sendmail(fromaddr, toaddr, text) 
    s.quit() 
    
def sendfileonemailtouser(usermail,cmpimg,ogpass):   
    fromaddr = "rockmike09@gmail.com"
    toaddr = usermail
    msg = MIMEMultipart()   
    msg['From'] = fromaddr  
    msg['To'] = toaddr   
    msg['Subject'] = "Cashless Campass"
    body = ogpass
    msg.attach(MIMEText(body, 'plain')) 
    filename = cmpimg
    attachment = open(cmpimg, "rb") 
    p = MIMEBase('application', 'octet-stream') 
    p.set_payload((attachment).read()) 
    encoders.encode_base64(p)    
    p.add_header('Content-Disposition', "attachment; filename= %s" % filename) 
    msg.attach(p) 
    s = smtplib.SMTP('smtp.gmail.com', 587) 
    s.starttls() 
    s.login(fromaddr, "ioptfwsacxmodyjs") 
    text = msg.as_string() 
    s.sendmail(fromaddr, toaddr, text) 
    s.quit()  

@app.route('/userRegister', methods=['GET', 'POST'])
def userRegister():
    if request.method == 'POST':
        print("GET")        

        fname = request.form.get("fname")
        lname = request.form.get("lname")        
        cid = request.form.get("cid")
        branch = request.form.get("branch")  
        year = request.form.get("year")
        email = request.form.get("email")        
        mobile = request.form.get("mobile")
        password = request.form.get("password")   
        fund = "0"
        print("INPUTS")        
        print("username",fname+" "+lname)
        
        cursor.execute('SELECT * FROM register WHERE cid = %s', (cid))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:   
            qrpath = 'static/QRs/'+str(cid)+'_QR.png'
            dataofqr = cid+"|"+branch+"|"+mobile+"|"+fund+"|"+qrpath
            img = qrcode.make(dataofqr)
            img.save(qrpath) 
            
            sql1 = "INSERT INTO register(fname,lname,cid,branch,year,email,mobile,password) VALUES (%s, %s, %s, %s, %s, %s, %s, %s);"
            val1 = (fname,lname,cid,branch,year,email,mobile,password)
            cursor.execute(sql1,val1)
            con.commit()
            return "success" 
    
@app.route('/userLogin', methods=['GET', 'POST'])
def userLogin():
    if request.method == 'POST':
        print("GET")        

        cid = request.form.get("cid")
        passw = request.form.get("password")       
        print("INPUTS")        
        print("username",cid)
        
        cursor.execute('SELECT * FROM register WHERE BINARY cid = %s AND BINARY password = %s', (cid, passw))
        count = cursor.rowcount
        if count > 0:
            return "success"
        else:
            return "Fail"
        
def splitFileName(split_filename):       
    basename, extension = os.path.splitext(split_filename)
    basename_parts = basename.split('-', 1) 

    catgoryname, image_name = basename_parts
    print("catgoryname:", catgoryname)
    print("Image:", image_name + extension)
    
    return catgoryname,image_name + extension
        
@app.route('/uploadimg', methods=['GET', 'POST'])
def uploadimg():
    if request.method == 'POST':
        f2= request.files['bill']
        
        filename_secure = secure_filename(f2.filename)
        split_filename = filename_secure.split('_')[-1]
        
        catgoryname,file = splitFileName(split_filename)
        f2.save('static/'+catgoryname+'/'+file)
        
        return "success"
    
@app.route('/uploadItems', methods=['GET', 'POST'])
def uploadItems():
    if request.method == 'POST':
        print("GET")        

        filename = request.form.get("filename")      
        title = request.form.get("title")    
        price = request.form.get("price")  
        category = request.form.get("category")
        print(filename)
        catgoryname,file = splitFileName(filename)
        
        cursor.execute('SELECT * FROM items WHERE name = %s AND category = %s', (title,category))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:                     
            sql1 = "INSERT INTO items(filename,name,price,category) VALUES (%s, %s, %s, %s);"
            val1 = ("static/"+catgoryname+"/"+file, title, price, category)
            cursor.execute(sql1,val1)
            con.commit()
            return "success" 
        
@app.route('/getAllItems', methods=['GET', 'POST'])
def getAllItems():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username") 
        print(username)
        
        cursor.execute('SELECT * FROM items WHERE category != %s',("Event"))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/getAllEvents', methods=['GET', 'POST'])
def getAllEvents():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username") 
        print(username)
        
        cursor.execute('SELECT * FROM items WHERE category = %s',("Event"))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/updateItems', methods=['GET', 'POST'])
def updateItems():
    if request.method == 'POST':
        print("GET")        

        idofitem = request.form.get("id") 
        filename = request.form.get("filename")     
        title = request.form.get("title")    
        price = request.form.get("price")  
        category = request.form.get("category")
        print(filename)
        catgoryname,file = splitFileName(filename)
                            
        sql1 = "UPDATE items SET filename = %s,name = %s,price = %s,category = %s WHERE id = %s;"
        val1 = ("static/"+catgoryname+"/"+file, title, price, category,idofitem)
        cursor.execute(sql1,val1)
        con.commit() 
        return "success" 
    
@app.route('/deleteItem', methods=['GET', 'POST'])
def deleteItem():
    if request.method == 'POST':        

        idoftpo = request.form.get("id")  
        name = request.form.get("name")
           
        sql11 = 'DELETE FROM items WHERE id = %s AND name = %s;'
        val11 = (idoftpo,name)
        cursor.execute(sql11,val11)
        con.commit()  
        
        return "success"
    
@app.route('/getAllItems1', methods=['GET', 'POST'])
def getAllItems1():
    if request.method == 'POST':
        print("GET")        

        category = request.form.get("category") 
        print(category)
        
        cursor.execute('SELECT * FROM items WHERE category = %s',(category))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/paybill', methods=['GET', 'POST'])
def paybill():
    if request.method == 'POST':
        print("GET")        

        pname = request.form.get("pname")
        pcat = request.form.get("pcat")  
        pprize = request.form.get("pprize")
        scandata = request.form.get("scandata")  
        userid = request.form.get("userid")   
        
        parts = scandata.split("|")
        print(parts)
        
        if str(parts[0]) != userid:            
            return "wrongscan"
        elif int(pprize) > int(parts[3]):
            return "fail"
        else:     
            current_time = datetime.now()  
            time_stamp = current_time.timestamp() 
            date_time = datetime.fromtimestamp(time_stamp)
            current_date_time = date_time.strftime("%d-%m-%Y %H:%M")  
              
            sql1 = "INSERT INTO orders(studid,studcat,pname,pcat,price,datetime) VALUES (%s, %s, %s, %s, %s, %s);"
            val1 = (parts[0], parts[1], pname, pcat, pprize, current_date_time)
            cursor.execute(sql1,val1)
            con.commit()
            
            updated_funds = str(int(parts[3])-int(pprize))
            print(updated_funds)
            dataofqr = parts[0]+"|"+parts[1]+"|"+parts[2]+"|"+updated_funds+"|"+parts[4]
            img = qrcode.make(dataofqr)
            img.save(str(parts[4]))
            return updated_funds+"&&"+pprize
        
@app.route('/updateFunds', methods=['GET', 'POST'])
def updateFunds():
    if request.method == 'POST':
        print("GET")        

        cid = request.form.get("idno")
        fund = request.form.get("fund")
        
        cursor.execute('SELECT * FROM register WHERE BINARY cid = %s', (cid))
        count = cursor.rowcount
        if count > 0:    
            qrpath = 'static/QRs/'+str(cid)+'_QR.png'
            image = cv2.imread(qrpath)
            qr_code_detector = cv2.QRCodeDetector()
            decoded_data, points, _ = qr_code_detector.detectAndDecode(image)

            parts = decoded_data.split("|")
            updated_funds = str(int(fund)+int(parts[3]))
            dataofqr = parts[0]+"|"+parts[1]+"|"+parts[2]+"|"+updated_funds+"|"+parts[4]
            img = qrcode.make(dataofqr)
            img.save(str(parts[4])) 
            
            return "success"
        else:
            return "fail"
        
@app.route('/issuebook', methods=['GET', 'POST'])
def issuebook():
    if request.method == 'POST':
        print("GET")        

        pname = request.form.get("pname")
        pcat = request.form.get("pcat")  
        userid = request.form.get("userid") 
        
        cursor.execute('SELECT * FROM orders WHERE studid = %s AND pname = %s AND pcat = %s', (userid,pname,pcat))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:
            cursor.execute('SELECT branch FROM register WHERE cid = %s', (userid))
            count = cursor.rowcount
            if count > 0:                
                current_time = datetime.now()  
                time_stamp = current_time.timestamp() 
                date_time = datetime.fromtimestamp(time_stamp)
                current_date_time = date_time.strftime("%d-%m-%Y %H:%M") 
                
                row = cursor.fetchone()
                sql1 = "INSERT INTO orders(studid,studcat,pname,pcat,price,datetime) VALUES (%s, %s, %s, %s, %s, %s);"
                val1 = (userid, row[0], pname, pcat, '-', current_date_time)
                cursor.execute(sql1,val1)
                con.commit()
                return "success"
            else:
                return "fail"
            
@app.route('/getBalance', methods=['GET', 'POST'])
def getBalance():
    if request.method == 'POST':        

        cid = request.form.get("cid")  
        qrpath = 'static/QRs/'+str(cid)+'_QR.png'
        image = cv2.imread(qrpath)
        qr_code_detector = cv2.QRCodeDetector()
        decoded_data, points, _ = qr_code_detector.detectAndDecode(image)

        parts = decoded_data.split("|")
        
        return str(parts[3])
    
@app.route('/getHistory', methods=['GET', 'POST'])
def getHistory():
    if request.method == 'POST':
        print("GET")        

        userid = request.form.get("userid") 
        print(userid)
        
        cursor.execute('SELECT * FROM orders WHERE studid = %s',(userid))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/getAllUsers', methods=['GET', 'POST'])
def getAllUsers():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username") 
        print(username)
        
        cursor.execute('SELECT * FROM register')
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/getAllHistory', methods=['GET', 'POST'])
def getAllHistory():
    if request.method == 'POST':
        print("GET")        

        userid = request.form.get("userid") 
        print(userid)
        
        cursor.execute('SELECT * FROM orders')
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)      
        
        return jsonObj
    
@app.route('/sendQuery', methods=['GET', 'POST'])
def sendQuery():
    if request.method == 'POST':
        print("GET")        

        cid = request.form.get("cid")
        query = request.form.get("query")       
        print("INPUTS")        
        print("username",cid)
        
        cursor.execute('SELECT * FROM register WHERE BINARY cid = %s', (cid))
        count = cursor.rowcount
        if count > 0:
            data = cursor.fetchone()  
            message = "Dear Admin, "+data[1]+" "+data[2]+" send you query having "+data[4]+" Branch and "+cid+" cid. The query is "+query
            sendemailtouser(adminmail,message)
            return "success"
        else:
            return "Fail"
        
@app.route('/makeNotification', methods=['GET', 'POST'])
def makeNotification():
    if request.method == 'POST':
        print("GET")        

        filename = request.form.get("filename") 
        title = request.form.get("title")    
        catgoryname = request.form.get("catgoryname")    
        print("INPUTS")        
        print("title",title)
        
        catgoryname,file = splitFileName(filename)
        
        cursor.execute('SELECT * FROM register')
        count = cursor.rowcount
        if count > 0:
            data = cursor.fetchall() 
            for i in data:
                message = "Your examination is on "+title
                sendfileonemailtouser(i[6],"static/"+catgoryname+"/"+file,message)
            return "success"
        else:
            return "Fail"
        
@app.route('/getSumCount', methods=['GET', 'POST'])
def getSumCount():
    if request.method == 'POST':
        print("GET")        

        user = request.form.get("user") 
        print(user)
        
        librarydata = []
        cursor.execute('SELECT * FROM items WHERE category=%s',("Library"))
        count = cursor.rowcount
        if count > 0:
            row = cursor.fetchall()
            librarydata = [(item[1], item[2]) for item in row]
        
        canteendata = []
        cursor.execute('SELECT * FROM items WHERE category=%s',("Canteen"))
        count = cursor.rowcount
        if count > 0:
            row = cursor.fetchall()
            canteendata = [(item[1], item[2]) for item in row]
        
        stationarydata = []
        cursor.execute('SELECT * FROM items WHERE category=%s',("Stationary"))
        count = cursor.rowcount
        if count > 0:
            row = cursor.fetchall()
            stationarydata = [(item[1], item[2]) for item in row]
        
        eventdata = []
        cursor.execute('SELECT * FROM items WHERE category=%s',("Event"))
        count = cursor.rowcount
        if count > 0:
            row = cursor.fetchall()
            eventdata = [(item[1], item[2]) for item in row]


        librarysumcount = []
        for i in librarydata:
            cursor.execute('SELECT SUM(status) AS sum FROM orders WHERE pname = %s AND pcat = %s AND status != %s;',(i[1],"Library","-"))
            row = cursor.fetchall()
            total_sum = row[0][0] if row[0][0] is not None else 0
            i = list(i)
            i.append(int(total_sum))
            librarysumcount.append(i)
        
        canteensumcount = []
        for i in canteendata:
            cursor.execute('SELECT SUM(price) AS sum FROM orders WHERE pname = %s AND pcat = %s AND price != %s;',(i[1],"Canteen","-"))
            row = cursor.fetchall()
            total_sum = row[0][0] if row[0][0] is not None else 0
            i = list(i)
            i.append(int(total_sum))
            canteensumcount.append(i)
        
        stationarysumcount = []
        for i in stationarydata:
            cursor.execute('SELECT SUM(price) AS sum FROM orders WHERE pname = %s AND pcat = %s AND price != %s;',(i[1],"Stationary","-"))
            row = cursor.fetchall()
            total_sum = row[0][0] if row[0][0] is not None else 0
            i = list(i)
            i.append(int(total_sum))
            stationarysumcount.append(i)
        
        eventsumcount = []
        for i in eventdata:
            cursor.execute('SELECT SUM(price) AS sum FROM orders WHERE pname = %s AND pcat = %s AND price != %s;',(i[1],"Event","-"))
            row = cursor.fetchall()
            total_sum = row[0][0] if row[0][0] is not None else 0
            i = list(i)
            i.append(int(total_sum))
            eventsumcount.append(i)        
        
        jsonObj = json.dumps([librarysumcount,canteensumcount,stationarysumcount,eventsumcount])      
        # print(jsonObj)
        
        return jsonObj
        
def getBalance1(cid):
    qrpath = 'static/QRs/'+str(cid)+'_QR.png'
    image = cv2.imread(qrpath)
    qr_code_detector = cv2.QRCodeDetector()
    decoded_data, points, _ = qr_code_detector.detectAndDecode(image)

    parts = decoded_data.split("|")
    print(parts)
    return parts[3]
        
def my_function():
    print("Function called")
    
    cursor.execute('SELECT studid,pname,datetime FROM orders WHERE pcat = %s', ("Library"))
    count = cursor.rowcount
    notifylist = []
    finelist = []
    if count > 0:
        row = cursor.fetchall()
        for i in row:
            given_date = datetime.strptime(i[2], '%d-%m-%Y %H:%M')
            current_date = datetime.now()
            difference = current_date - given_date
            difference_in_days = difference.days
            cursor.execute('SELECT register.fname,register.lname,register.email,orders.studid,orders.pname,orders.status FROM register JOIN orders ON register.cid = orders.studid WHERE orders.studid = %s and orders.pname = %s and orders.datetime = %s', (i[0],i[1],i[2]))
            data = cursor.fetchone()
            if int(difference_in_days) == 6:
                notifylist.append(data)
            elif int(difference_in_days) > 7 and data[5] == "-":
                data = list(data)
                data.append(difference_in_days-7)
                data.append(given_date)
                finelist.append(data)
                
    for i in notifylist:
        print(i)
        sendemailtouser(i[2],"Dear "+str(i[0]+" "+i[1])+", Book '"+str(i[4])+"' return date is tomorrow.")

    for i in finelist:
        print(i)
        qrpath = 'static/QRs/'+str(i[3])+'_QR.png'
        image = cv2.imread(qrpath)
        qr_code_detector = cv2.QRCodeDetector()
        decoded_data, points, _ = qr_code_detector.detectAndDecode(image)
    
        parts = decoded_data.split("|")
        print(parts)
        updated_funds = str(int(parts[3]) - int(10*int(i[6])))
        dataofqr = parts[0]+"|"+parts[1]+"|"+parts[2]+"|"+updated_funds+"|"+parts[4]
        img = qrcode.make(dataofqr)
        img.save(str(parts[4])) 
        
        sql1 = "UPDATE orders SET status = %s WHERE studid = %s AND pname = %s;"
        val1 = (str(int(10*int(i[6]))), i[3], i[4])
        cursor.execute(sql1,val1)
        con.commit() 
        
        sendemailtouser(i[2],"Dear "+str(i[0]+" "+i[1])+", Rs."+str(int(10*int(i[6])))+" is deducted from you account for not submitting the book "+str(i[4])+" on time "+str(i[7]))
        # sendemailtouser(i[2],"Dear "+str(i[0]+" "+i[1])+", "+str(int(10*int(i[6])))+" Rs. is deducted from your account Remaining balance is "+str(updated_funds))
       
        # ---------------------------------------------------------------------------------------

    lowbalncelist = []
    cursor.execute('SELECT * FROM register')
    count = cursor.rowcount
    if count > 0:
        rowdata = cursor.fetchall()
        for row in rowdata:
            print(row)
            if int(getBalance1(row[3])) < 20:
                lowbalncelist.append(row)
                
    for i in lowbalncelist:
        print(i)
        sendemailtouser(i[6],"Dear "+str(i[1]+" "+i[2])+", You have low balance.")
        sendemailtouser(adminmail,"Dear Admin, "+str(i[1]+" "+i[2])+" having low balance.")

        # ---------------------------------------------------------------------------------------

schedule.every().day.at("13:39").do(my_function)

def scheduler_thread():
    while True:
        schedule.run_pending()
        time.sleep(10)
            
if __name__ == "__main__":
    scheduler = threading.Thread(target=scheduler_thread)
    scheduler.start()
    
    app.run("0.0.0.0")
    
    


