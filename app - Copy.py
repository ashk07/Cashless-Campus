from flask import Flask
from flask import request
import pymysql
from werkzeug.utils import secure_filename
import os
import json
from datetime import datetime 

app = Flask(__name__)

UPLOAD_FOLDER = 'static/schedule/'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

UPLOAD_FOLDER1 = 'static/imgvideo/'
app.config['UPLOAD_FOLDER1'] = UPLOAD_FOLDER1

app.secret_key = 'any random string'

def dbConnection():
    try:
        connection = pymysql.connect(host="localhost", user="root", password="root", database="cashlesscampus")
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

@app.route('/userRegister', methods=['GET', 'POST'])
def userRegister():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username")
        passw = request.form.get("password")        
        email = request.form.get("emailid")
        mobile = request.form.get("mobilenumber")        
        print("INPUTS")        
        print("username",username)
        
        cursor.execute('SELECT * FROM register WHERE username = %s', (username))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:   
            if 'kgce.edu.in' in email:
                sql1 = "INSERT INTO register(username, email,mobile, password) VALUES (%s, %s, %s, %s);"
                val1 = (username, email, mobile, passw)
                cursor.execute(sql1,val1)
                con.commit()
                return "success" 
            else:
                return "invalidmail" 
    
@app.route('/userLogin', methods=['GET', 'POST'])
def userLogin():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username")
        passw = request.form.get("password")       
        print("INPUTS")        
        print("username",username)
        
        cursor.execute('SELECT * FROM register WHERE username = %s AND password = %s', (username, passw))
        count = cursor.rowcount
        if count > 0:
            return "success"
        else:
            return "Fail"
        
"***********************************************************************************************************"
        
@app.route('/uploadimg', methods=['GET', 'POST'])
def uploadimg():
    if request.method == 'POST':
        f2= request.files['bill']
        
        filename_secure = secure_filename(f2.filename)
        split_filename = filename_secure.split('_')[-1]
        f2.save(os.path.join(app.config['UPLOAD_FOLDER'], split_filename))
        
        return "success"
    
@app.route('/uploadSchedule', methods=['GET', 'POST'])
def uploadSchedule():
    if request.method == 'POST':
        print("GET")        

        filename = request.form.get("filename")      
        title = request.form.get("title")  
        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM schedule WHERE title = %s AND category = %s', (title,category))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:                     
            sql1 = "INSERT INTO schedule(filename,title,category) VALUES (%s, %s, %s);"
            val1 = ("static/schedule/"+filename, title, category)
            cursor.execute(sql1,val1)
            con.commit()
            return "success" 
        
@app.route('/addNotice', methods=['GET', 'POST'])
def addNotice():
    if request.method == 'POST':
        print("GET")        

        notice = request.form.get("notice")      
        title = request.form.get("title")  
        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM addnotice WHERE title = %s AND category = %s', (title,category))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:                     
            sql1 = "INSERT INTO addnotice(notice,title,category) VALUES (%s, %s, %s);"
            val1 = (notice, title, category)
            cursor.execute(sql1,val1)
            con.commit()
            return "success" 

@app.route('/uploadimg1', methods=['GET', 'POST'])
def uploadimg1():
    if request.method == 'POST':
        f2= request.files['bill']
        
        filename_secure = secure_filename(f2.filename)
        split_filename = filename_secure.split('_')[-1]
        f2.save(os.path.join(app.config['UPLOAD_FOLDER1'], split_filename))
        
        return "success"        

@app.route('/getVideo', methods=['GET', 'POST'])
def getVideo():
    if request.method == 'POST':
        print("GET")        

        f2= request.files['uploaded_file']
        
        filename_secure = secure_filename(f2.filename)
        split_filename = filename_secure.split('_')[-1]
        f2.save(os.path.join(app.config['UPLOAD_FOLDER1'], split_filename))
        
        return "success"
    
@app.route('/calledfunction', methods=['GET', 'POST'])
def calledfunction():
    if request.method == 'POST':
        print("GET") 
        return "success"
    
@app.route('/uploadimgandvideo', methods=['GET', 'POST'])
def uploadimgandvideo():
    if request.method == 'POST':
        print("GET")        

        filename = request.form.get("filename")      
        title = request.form.get("title")  
        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM imgvideo WHERE title = %s AND category = %s', (title,category))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:                     
            sql1 = "INSERT INTO imgvideo(filename,title,category) VALUES (%s, %s, %s);"
            val1 = ("static/imgvideo/"+filename, title, category)
            cursor.execute(sql1,val1)
            con.commit()
            return "success"        
        
"***********************************************************************************************************"
        
@app.route('/viewScedule', methods=['GET', 'POST'])
def viewScedule():
    if request.method == 'POST':
        print("GET")        

        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM schedule WHERE category = %s', (category))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)
        # print(jsonObj)        
        
        return jsonObj
    
"***********************************************************************************************************"

@app.route('/viewCultureImgVid', methods=['GET', 'POST'])
def viewCultureImgVid():
    if request.method == 'POST':
        print("GET")        

        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM imgvideo WHERE category = %s', (category))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)
        # print(jsonObj)        
        
        return jsonObj
    
"***********************************************************************************************************"

@app.route('/viewCultureNotice', methods=['GET', 'POST'])
def viewCultureNotice():
    if request.method == 'POST':
        print("GET")        

        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM addnotice WHERE category = %s', (category))
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)
        # print(jsonObj)        
        
        return jsonObj

"***********************************************************************************************************"
    
@app.route('/checkEnroll', methods=['GET', 'POST'])
def checkEnroll():
    if request.method == 'POST':
        print("GET")        

        username = request.form.get("username")      
        title = request.form.get("title")  
        category = request.form.get("category")  
        
        cursor.execute('SELECT * FROM enrolldata WHERE username = %s AND title = %s AND category = %s', (username,title,category))
        count = cursor.rowcount
        if count > 0:
            return "fail"
        else:
            return "success" 
        
@app.route('/Enroll', methods=['GET', 'POST'])
def Enroll():
    if request.method == 'POST':
        print("GET") 
        
        current_time = datetime.now()  
        date_time = datetime.fromtimestamp(current_time.timestamp())

        username = request.form.get("username")      
        fname = request.form.get("fname")  
        email = request.form.get("email")  
        mobile = request.form.get("mobile")  
        branch = request.form.get("branch")      
        year = request.form.get("year")
        str_date_time = date_time.strftime("%d-%m-%Y %H:%M:%S") 
        title = request.form.get("title")  
        category = request.form.get("category") 
        
        print(str_date_time)
    
        sql1 = "INSERT INTO enrolldata(username,fname,email,mobile,branch,year,timestamp,title,category) VALUES (%s, %s, %s,%s, %s, %s,%s, %s, %s);"
        val1 = (username,fname,email,mobile,branch,year,str(str_date_time),title,category)
        cursor.execute(sql1,val1)
        con.commit()
        return "success"
 
"***********************************************************************************************************"
   
@app.route('/getEnrolledData', methods=['GET', 'POST'])
def getEnrolledData():
    if request.method == 'POST':
        print("GET")         
        
        cursor.execute('SELECT * FROM enrolldata')
        row = cursor.fetchall() 
        
        jsonObj = json.dumps(row)
        # print(jsonObj)        
        
        return jsonObj
    
"***********************************************************************************************************"
   
@app.route('/deleteData', methods=['GET', 'POST'])
def deleteData():
    if request.method == 'POST':
        print("GET")  
        idof = request.form.get("id") 
        
        sql11 = 'DELETE FROM enrolldata WHERE id = %s'
        val11 = (idof)
        cursor.execute(sql11,val11)
        con.commit() 
        
        return "success"
        
    
if __name__ == "__main__":
    app.run("0.0.0.0")
    
    


