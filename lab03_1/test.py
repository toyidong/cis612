import MySQLdb

db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="ethanCN18",  # your password
                     db="UnionAddress")   # name of the data base
cursor = db.cursor()

query1 = '''insert into Union_Address values ("a","b","c","d","e");'''
cursor.execute(query1)

with open('table.csv','rb') as csvfile:
    # reader = csv.reader(csvfile, delimiter='\t')
    next(csvfile)
    for row in csvfile:
        tokens = row.split('\t')
        cursor.execute("INSERT INTO Union_Address VALUES (%s, %s, %s, %s, %s);",tokens)
        db.commit()

db.close()