from gevent import monkey
monkey.patch_all()

import MySQLdb

from flask import Flask
from flask import jsonify
from flask import request

import time

app = Flask(__name__)

@app.route('/',methods = ['POST'])
def hello_world():
    return jsonify(action="test",status=1,data="")

@app.route('/billingbilling/api/register',methods = ['POST'])
def register():

    try:
        user_info = {
            "user_name":request.json['uname'],
            "user_pwd":request.json['pwd']
        }

        print 1
        conn = MySQLdb.connect(host='127.0.0.1',user='root',passwd='123cj',db='billingbilling',charset='utf8')
        print 2
        cursor = conn.cursor()
        print 3
        sqlQuery = "select * from user_baseinfo_table where user_name = '%s'" % (user_info["user_name"])
        print 4
        n = cursor.execute(sqlQuery)
        print 5
        print 'n=',n
        if n > 0:
            return jsonify(action='register',status=2,data='')

        print 6
        sqlInsert = "insert into user_baseinfo_table(user_name,user_pwd) values('%s','%s')" % (user_info["user_name"],user_info["user_pwd"])
        n = cursor.execute(sqlInsert)

        sqlQuery = "select * from user_baseinfo_table where user_name = '%s'" % (user_info["user_name"])
        cursor.execute(sqlQuery)
        user = cursor.fetchone()

        print 7
        if n > 0:
            conn.commit()
            return jsonify(action='register',status=1,data='',uid=user[0],uname=user[1])

    except Exception as e:
        print e.message
    finally:
       conn.close()

    return jsonify(action='register',status=3,data='')


@app.route('/billingbilling/api/login',methods = ['POST'])
def login():
    print request.json
    user_info = {
        "user_name":request.json['uname'],
        "user_pwd":request.json['pwd'],
    }
    print 1
    try:
        print 2
        conn = MySQLdb.connect(host='127.0.0.1',user='root',passwd='123cj',db='billingbilling',charset='utf8')
        print 2
        cursor = conn.cursor()
        sqlQuery = "select * from user_baseinfo_table where user_name = '%s'" % (user_info["user_name"])
        n = cursor.execute(sqlQuery)
        print 3
        if n == 0:
            return jsonify(action='login',status=2,data='')

        user = cursor.fetchone()
        print 4
        print user
        if user[2] <> user_info["user_pwd"]:
            return jsonify(action='login',status=3,data='')
        print 5

        sqlQuery = "select * from pay_record_table where user_id = '%d'" % (user[0])
        n = cursor.execute(sqlQuery)

        payList = cursor.fetchall()

        print payList

        print 6
        sqlQuery = "select * from income_record_table where user_id = '%d'" % (user[0])
        cursor.execute(sqlQuery)
        incomeList = cursor.fetchall()

        print 7
        sqlQuery = "select * from account_category_table where user_id = '%d'" % (user[0])
        cursor.execute(sqlQuery)
        categoryList = cursor.fetchall()

        print 8
        return jsonify(action='login',status=1,data=[payList,incomeList,categoryList],uid = user[0],uname = user[1])

    except Exception as e:
        print e.message
    finally:
        conn.close()

    return jsonify(action='login',status='4',data='')

@app.route("/billingbilling/api/addModule",methods = ["POST"])
def addModule():

    try:
        moduleadd_info = {
            "uid":request.json['uid'],
            "cname":request.json['category_name'],
            "cflag":request.json['category_flag']
        }
        print 1

        conn = MySQLdb.connect(host='127.0.0.1',user='root',passwd='123cj',db='billingbilling',charset='utf8')
        cursor = conn.cursor()

        print 2

        sqlQuery = "select * from account_category_table where category_name = '%s'" % (moduleadd_info["cname"])
        n = cursor.execute(sqlQuery)

        rlt = cursor.fetchone()
        if n > 0 and int(rlt[2]) == int(moduleadd_info["cflag"]) and int(rlt[3]) == int(moduleadd_info['uid']):
            return jsonify(action="addModule",status=2,data='')

        print 4

        sqlInsert = "insert into account_category_table(category_name,category_flag,user_id) values('%s','%d','%d')" % \
                    (moduleadd_info["cname"],int(moduleadd_info["cflag"]),int(moduleadd_info["uid"]))
        n = cursor.execute(sqlInsert)
        print 5

        if n > 0:
            return jsonify(action="addModule",status=1,data='',cid=conn.insert_id())

    except Exception as e:
        print e.message
    finally:
        conn.commit()
        conn.close()

    return jsonify(action="addModule",status=3,data='')

@app.route("/billingbilling/api/addAccount", methods = ['POST'])
def addCount():

    try:
        print 0
        account_info = {
            "uid":request.json['uid'],
            "describe":request.json['describe'],
            "flag":request.json['flag'],
            "money":request.json['money'],
            "category":request.json['category'],
            'time':time.strftime('%Y-%m-%d',time.localtime(time.time()))
        }

        conn = MySQLdb.connect(host='127.0.0.1',user='root',passwd='123cj',db='billingbilling',charset='utf8')

        cursor = conn.cursor()

        if int(account_info["flag"]) == 0:
            sqlInsert = "insert into pay_record_table(pay_name,pay_money,pay_date,user_id,category_id) values('%s','%f','%s','%d','%d')" % \
                        (account_info["describe"],float(account_info["money"]),account_info["time"],int(account_info["uid"]),int(account_info["category"]))
        else:
            sqlInsert = "insert into income_record_table(inc_name,inc_money,inc_date,user_id,category_id) values('%s','%f','%s','%d','%d')" % \
                        (account_info["describe"],float(account_info["money"]),account_info["time"],int(account_info["uid"]),int(account_info["category"]))

        print request.json
        n = cursor.execute(sqlInsert)

        if n == 0:
            return jsonify(action='addAccount',status=2,data='')

        print conn.insert_id()

        return jsonify(action='addAccount',status=1,data='',id=conn.insert_id())
    except Exception as e:
        print e
    finally:
        conn.commit()
        conn.close()
    return jsonify(action='addAccount',status=2,data='')



if __name__ == '__main__':
    app.run()
