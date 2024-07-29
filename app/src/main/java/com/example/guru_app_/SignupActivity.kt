package com.example.guru_app_

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import javax.security.auth.callback.PasswordCallback

class SignupActivity : AppCompatActivity() {
    lateinit var dbHelper: DBHelper
    lateinit var back: TextView
    lateinit var idcheckbtn: AppCompatButton
    lateinit var signupbtn: AppCompatButton
    lateinit var id: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText
    lateinit var name: EditText
    lateinit var birth: EditText
    lateinit var mail: EditText
    var CheckId: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        dbHelper = DBHelper(this)
        back = findViewById(R.id.back)
        idcheckbtn = findViewById(R.id.idcheckbutton)
        signupbtn = findViewById(R.id.signupbutton)

        id = findViewById(R.id.user_id)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.password2)
        name = findViewById(R.id.name)
        birth = findViewById(R.id.birth)
        mail = findViewById(R.id.mail)

        // 버튼 클릭 리스너를 설정합니다.
        back.setOnClickListener {
            // 새로운 인텐트를 생성하여 SignupActivity로 이동합니다.
            //var할시 오류.. 초기화문제 한번만 사용되기 때문에. 다시 돌아오는경우엔?
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        idcheckbtn.setOnClickListener{
            val userid = id.text.toString()
            if(userid == ""){
                Toast.makeText(this@SignupActivity,
                    "아이디를 입력해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val checkid = dbHelper.checkID(userid)
                if(checkid == false){
                    CheckId = true
                    Toast.makeText(this@SignupActivity,
                        "사용 가능한 아이디입니다.",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@SignupActivity,
                        "이미 존재하는 아이디입니다.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        signupbtn.setOnClickListener{
            val userid = id.text.toString()
            val pass = password.text.toString()
            val repass = repassword.text.toString()
            val name = name.text.toString()
            val birth = birth.text.toString()
            val mail = mail.text.toString()

            if(userid == "" || pass == "" || repass == "" || name == "" ||
                birth == "" || mail == "")
                Toast.makeText(this@SignupActivity,
                    "회원정보를 모두 입력해주세요.",
                    Toast.LENGTH_SHORT).show()
            else{
                //아이디 중복 확인 O
                if( CheckId == true){
                    // 비밀번호 재확인 O
                    if(pass == repass){
                        val insert = dbHelper.insertData(userid, pass, name, birth, mail)
                        //가입 성공시 메인화면으로 전환
                        if(insert == true) {
                            Toast.makeText(
                                this@SignupActivity,
                                "회원가입을 축하합니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        //가입 실패
                        else{
                            Toast.makeText(
                                this@SignupActivity,
                                "가입에 실패하였습니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    //비밀번호 재확인 실패
                    else{
                        Toast.makeText(
                            this@SignupActivity,
                            "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                //아이디 중복확인 X
                else{
                    Toast.makeText(this@SignupActivity,
                        "아이디 중복확인을 해주세요",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}