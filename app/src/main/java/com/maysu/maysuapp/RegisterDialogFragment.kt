package com.maysu.maysuapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth

class RegisterDialogFragment : DialogFragment() {

    private lateinit var auth: FirebaseAuth

    // Compañero de clase para manejar la creación del diálogo con datos
    companion object {
        fun newInstance(email: String, pass: String): RegisterDialogFragment {
            val fragment = RegisterDialogFragment()
            val args = Bundle()
            args.putString("EMAIL", email)
            args.putString("PASS", pass)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        auth = FirebaseAuth.getInstance()

        // Inflar el diseño que corregimos antes
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_register, null)

        val etEmailReg = view.findViewById<EditText>(R.id.etEmailRegister)
        val etPasswordReg = view.findViewById<EditText>(R.id.etPasswordRegister)
        val etConfirmPass = view.findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        // Recuperar los datos enviados desde CorreoActivity
        val emailRecibido = arguments?.getString("EMAIL") ?: ""
        val passRecibida = arguments?.getString("PASS") ?: ""

        // Llenar los campos automáticamente
        etEmailReg.setText(emailRecibido)
        etPasswordReg.setText(passRecibida)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnRegister.setOnClickListener {
            val email = etEmailReg.text.toString().trim()
            val pass = etPasswordReg.text.toString().trim()
            val confirmPass = etConfirmPass.text.toString().trim()

            // Validaciones básicas
            if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(context, "La contraseña es muy corta (mínimo 6)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuario(email, pass)
        }

        return dialog
    }

    private fun registrarUsuario(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()

                // Ir al Home directamente
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                dismiss()
                activity?.finish() // Cerramos CorreoActivity
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}