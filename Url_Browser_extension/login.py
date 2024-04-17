import tkinter as tk
import requests
import cx_Oracle
import time
from tkinter import *

class Login:
    def __init__(self):
        self.parent = tk.Tk()
        self.parent.title("Kavach- (Extension Login)")
        self.parent.geometry("1500x800")
        self.parent.configure(bg="black")

        self.center_frame = tk.Frame(self.parent, bg="black")
        self.center_frame.pack(fill="both", expand=True)

        title_label = tk.Label(self.center_frame, text="Enter credentials", font=("algerian", 36, "bold"), fg="gold", bg="black")
        title_label.place(x=550, y=100)

        self.error_label = tk.Label(self.center_frame, text="", font=("Algerian", 16), bg="black")
        self.error_label.pack(pady=10)

        email_label = tk.Label(self.center_frame, text="Email:", font=("algerian", 16), fg="goldenrod", bg="black")
        email_label.place(x=715, y=200)

        self.username = tk.StringVar()
        self.email_entry = tk.Entry(self.center_frame, textvariable=self.username, font=("Arial", 16))
        self.email_entry.place(x=630, y=250)

        password_label = tk.Label(self.center_frame, text="Password:", font=("Algerian", 16), fg="goldenrod", bg="black")
        password_label.place(x=700, y=320)

        self.password = tk.StringVar()
        self.password_entry = tk.Entry(self.center_frame, show="*", font=("Arial", 16), textvariable=self.password)
        self.password_entry.place(x=630, y=370)

        self.toggle_button = tk.Button(self.center_frame, text="Show Password", command=self.toggle_password, bg="black", fg="goldenrod")
        self.toggle_button.place(x=700, y=405)

        login_button = tk.Button(self.center_frame, text="Login", font=("Algerian", 16), bg="gold", fg="black", command=self.validate_login)
        login_button.place(x=720, y=470)

        register_button = tk.Button(self.center_frame, text="Register", font=("Algerian", 16), bg="gold", fg="black", command=self.register)
        register_button.place(x=700, y=520)

        continue_button = tk.Button(self.center_frame, text="Continue Without Login", font=("Algerian", 16), bg="gold", fg="black", command=self.continue_without_login)
        continue_button.place(x=620, y=570)
        
        self.parent.mainloop()


    def validate_login(self):
        a = self.username.get()
        b = self.password.get()
        try:
            # Connect to Oracle database
            conn = cx_Oracle.connect('c##vishaal/shiva123@localhost:1521/XE')  # Update with your Oracle database connection details
            cursor = conn.cursor()
            cursor.execute("select * from Credentials where Id = :1 and Password = :2", (a, b))
            row = cursor.fetchone()
            if row is not None:
                # Login successful
                self.error_label.config(text="Login successful !", fg="green")
                self.parent.after(2000, self.parent.destroy)
                self.send_username_to_server(a)
                # Insert code for further actions upon successful login
            else:
                self.error_label.config(text="Invalid username or password", fg="red")
        except cx_Oracle.DatabaseError as e:
            print(f"An error occurred: {e.args[0]}")
        finally:
            if conn:
                conn.close()

    def continue_without_login(self):
    # Add code here to handle continuing without login
        self.error_label.config(text="Continuing without login...", fg="green")
        # Insert code for further actions when continuing without login
        self.parent.after(2000, self.parent.destroy)
        # Send a message to the server
        self.send_message_to_server({'action':'continue_without_login'})

    def send_message_to_server(self, message):
        # Assuming you have an API endpoint '/api/extension-data' in your server to receive messages
        server_url = 'http://localhost:3000/api/extension-data'  # Update with your server URL
        headers = {'Content-Type': 'application/json'}
        try:
            response = requests.post(server_url, json=message, headers=headers)
            if response.status_code == 200:
                print('Message sent to server successfully')
            else:
                print('Error sending message to server:', response.status_code)
        except Exception as e:
            print('Error sending message to server:', e)

    def send_username_to_server(self, username):
        server_url = 'http://localhost:3000/api/extension-data'
        headers = {'Content-Type': 'application/json'}
        message = {'action': 'login_successful', 'username': username}
        try:
            print('Sending message to server:', message)  # Debugging print statement
            response = requests.post(server_url, json=message, headers=headers)
            if response.status_code == 200:
                print('Username sent to server successfully')
            else:
                print('Error sending username to server:', response.status_code)
                print('Response:', response.text)  # Print the response for debugging
        except requests.exceptions.RequestException as e:
            print('Error sending username to server:', e)        
    
    def toggle_password(self):
        if self.password_entry.cget("show") == "*":
            self.password_entry.config(show="")
            self.toggle_button.config(text="Hide Password")
        else:
            self.password_entry.config(show="*")
            self.toggle_button.config(text="Show Password")
    
    def register(self):
        RegistrationPage(tk.Toplevel())
        
    
class RegistrationPage:
    def __init__(self, root):
        self.root = root
        self.root.title("Kavach -(Registration)")
        self.root.geometry("1500x800")
        self.background_frame = tk.Frame(self.root, bg="black")
        self.background_frame.pack(fill="both", expand=True)

        title_label = tk.Label(self.background_frame, text="Create new account", font=("algerian", 36, "bold"), fg="gold", bg="black")
        title_label.place(x=550, y=100)

        label_name = tk.Label(self.background_frame, text="Name", font=("algerian", 18), fg="gold", bg="black")
        label_name.place(x=550, y=250)

        label_email = tk.Label(self.background_frame, text="Email", font=("algerian", 18), fg="gold", bg="black")
        label_email.place(x=550, y=330)

        label_password = tk.Label(self.background_frame, text="Password", font=("algerian", 18), fg="gold", bg="black")
        label_password.place(x=550, y=410)

        label_phone = tk.Label(self.background_frame, text="Phone number", font=("algerian", 18), fg="gold", bg="black")
        label_phone.place(x=550, y=490)

        self.entry_name = tk.Entry(self.background_frame, font=("helvetica", 16), bg="white")
        self.entry_name.place(x=750, y=250)

        self.entry_email = tk.Entry(self.background_frame, font=("helvetica", 16), bg="white")
        self.entry_email.place(x=750, y=330)

        self.entry_password = tk.Entry(self.background_frame, font=("helvetica", 16), bg="white")
        self.entry_password.place(x=750, y=410)

        self.entry_phone = tk.Entry(self.background_frame, font=("helvetica", 16), bg="white")
        self.entry_phone.place(x=750, y=490)

        register_button = tk.Button(self.background_frame, text="Register", font=("algerian", 16), bg="gold", fg="black", command=self.submit)
        register_button.place(x=770, y=275 + 320)
        back_button = tk.Button(self.background_frame, text="Back", font=("algerian", 16), bg="gold", fg="black", command=self.go_back)
        back_button.place(x=670, y=275 + 320)
        self.root.mainloop()


    def submit(self):
        a = self.entry_name.get()
        b = self.entry_email.get()
        c = self.entry_password.get()
        d = self.entry_phone.get()

        if not d.isdigit() or len(d) != 10:
            error_label = tk.Label(self.background_frame, text="Enter a valid phone number", font=("algerian", 16), fg="red")
            error_label.place(x=670, y=550)
            return

        try:
            # Connect to Oracle database
            conn = cx_Oracle.connect('c##vishaal/shiva123@localhost:1521/XE')  # Update with your Oracle database connection details
            cursor = conn.cursor()
            cursor.execute("insert into Credentials (Id, Password, Name, Phone) values (:1, :2, :3, :4)", (b, c, a, d))
            conn.commit()
            success_label = tk.Label(self.background_frame, text="User data added successfully!", font=("algerian", 16), fg="green")
            success_label.place(x=670, y=550)

        except cx_Oracle.DatabaseError as e:
            print(f"An error occurred: {e.args[0]}")
        finally:
            if conn:
                conn.close()
                self.root.destroy()

    def go_back(self):
        self.root.destroy()
            
if __name__ ==  "__main__":
    page = Login()
