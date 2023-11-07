/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class Page_RSA extends javax.swing.JFrame {

    private static BigInteger d;
    private static BigInteger n;
    private static BigInteger e;
    private static BigInteger phi;
    private static BigInteger keyPublic;
    private static BigInteger keyPrivate;
    private static BigInteger Encrypted;
    private static BigInteger Dercypted;

    /**
     * Creates new form Page_RSA
     */
    public Page_RSA() {
        initComponents();
        //BỎ TRONG SLIDE từ 1 đến 5 thôi
        // 1. Tìm 2 số nguyên tố p and q
        BigInteger p = largePrime(512);
        BigInteger q = largePrime(512);

        // 2. Tính n = p*q
        // n mod cho khóa riêng và khóa chung, độ dài n bit là độ dài khóa
        n = p.multiply(q);

        // 3. Tính Phi(n) 
        // Phi(n) = (p-1)(q-1)
        // BigIntegers đối tượng và phải sử dụng các phương thức cho các phép tính đại số
        phi = getPhi(p, q);
        // 4. Tìm int e sao cho 1 < e < Phi(n) và gcd(e,Phi) = 1
        e = genE(phi);
        // 5. Tính d khi  d ≡ e^(-1) (mod Phi(n)) khóa bí mật
        d = e.modInverse(phi); // tính nghịch đảo modular
    }

    /**
     * //BỎ TRONG SLIDE
     * Lấy một chuỗi và chuyển đổi từng ký tự thành giá trị thập phân ASCII 
     * Returns BigInteger
     */
    public static BigInteger convertDigToNumber(String message) {
        message = message.toUpperCase(); // Chuyển thông điệp thành kiểu in hoa
        String cipherString = "";
        int i = 0;
        while (i < message.length()) {
            int ch = (int) message.charAt(i); //Lấy từng kí tự và chuyển nó thành giá trị số nguyên (ASCII) 
            cipherString = cipherString + ch;
            i++;
        }
        BigInteger cipherBig = new BigInteger(String.valueOf(cipherString));
        return cipherBig;
    }

    /**
     * //BỎ TRONG SLIDE
     * Lấy một số bigInteger được mã hóa, chuyển nó thành dạng văn bản returns a
     * String
     */
    public static String convertNumberToDig(BigInteger message) {
        String cipherString = message.toString();
        String output = "";
        int i = 0;
        while (i < cipherString.length()) {
            int temp = Integer.parseInt(cipherString.substring(i, i + 2));
            char ch = (char) temp;
            output = output + ch;
            i = i + 2;
        }
        return output;
    }

    /**
     * 3. Tính Phi(n) Phi(n) = (p-1)(q-1) BigIntegers đối tượng và phải sử dụng
     * các phương thức cho các phép tính đại số
     */
    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return phi;
    }

    /**
     * Tạo số nguyên tố lớn ngẫu nhiên có độ dài bit được chỉ định
     *
     */
    public static BigInteger largePrime(int bits) {
        Random randomInteger = new Random();
        BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger); // Tạo ra các số nguyên tố ngẫu nhiên
        return largePrime;
    }

    /**
     * Đệ quy tìm ước chung lớn nhất
     * denominator Note: Uses BigInteger operations
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }

    /**
     * //BỎ TRONG SLIDE
     * tạo e bằng cách tìm  Phi sao cho chúng là các số nguyên tố cùng nhau , ước chung  = 1(gcd = 1)
     *
     */
    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e = new BigInteger(1024, rand);
        do {
            e = new BigInteger(1024, rand); 
            while (e.min(phi).equals(phi)) { // khi phi nhỏ hơn e, hãy tìm e mới
                e = new BigInteger(1024, rand);
            }
        } while (!gcd(e, phi).equals(BigInteger.ONE)); // nếu gcd(e,phi) không bằng 1 thì tiếp tục vòng lặp
        return e;
    }

    //BỎ TRONG SLIDE
    // Phương thức tự triển khai modPow 
    private static BigInteger modPows(BigInteger base, BigInteger exponent, BigInteger modulus) {
        if (exponent.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }

        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.and(BigInteger.ONE).equals(BigInteger.ONE)) {
                result = (result.multiply(base)).mod(modulus);
            }
            exponent = exponent.shiftRight(1);
            base = (base.multiply(base)).mod(modulus);
        }

        return result;
    }

    //Hàm mã hóa //BỎ TRONG SLIDE
    public void Encrypted() {
        txt_KeyPublic.setText("{" + e + "," + n + "}");
        txt_KeyPrivate.setText("{" + d + "," + n + "}");

        // chuyển từ ký tự chữ sang số
        BigInteger cipherMessage = convertDigToNumber(txt_PlainText2.getText()); 

        // mã hóa thông điệp cipher
        Encrypted = modPows(cipherMessage, e, n);
        txt_EncryptedMess1.setText(Encrypted + "");
    }
    
    //Hàm giải mã   //BỎ TRONG SLIDE
    public void Dercypted() {
        // Giải mã thông điệp đã mã hóa 
        Dercypted = modPows(Encrypted, d, n);

        // Chuyển từ số sang chữ
        String restoredMessage = convertNumberToDig(Dercypted);

        txt_DercyptedMess1.setText(restoredMessage + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_Chinh = new javax.swing.JPanel();
        txt_KeyPrivate = new javax.swing.JTextField();
        lbl_KeyPrivate = new javax.swing.JLabel();
        lbl_tieuDe = new javax.swing.JLabel();
        btn_Res = new javax.swing.JButton();
        txt_KeyPublic = new javax.swing.JTextField();
        lbl_keyPublic = new javax.swing.JLabel();
        txt_PlainText2 = new javax.swing.JTextField();
        lbl_Plain = new javax.swing.JLabel();
        btn_MaHoa1 = new javax.swing.JButton();
        btn_giaiMa1 = new javax.swing.JButton();
        txt_DercyptedMess1 = new javax.swing.JTextField();
        txt_EncryptedMess1 = new javax.swing.JTextField();
        lbl_Encry = new javax.swing.JLabel();
        lbl_Decry = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_KeyPrivate.setText("Key Private:");

        lbl_tieuDe.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbl_tieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_tieuDe.setText("RSA");

        btn_Res.setBackground(new java.awt.Color(102, 255, 51));
        btn_Res.setText("Reset");
        btn_Res.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Res.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ResActionPerformed(evt);
            }
        });

        lbl_keyPublic.setText("Key Public:");

        lbl_Plain.setText("Plain text");

        btn_MaHoa1.setBackground(new java.awt.Color(102, 102, 255));
        btn_MaHoa1.setText("Giải mã");
        btn_MaHoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MaHoa1ActionPerformed(evt);
            }
        });

        btn_giaiMa1.setBackground(new java.awt.Color(102, 255, 102));
        btn_giaiMa1.setText("Mã hóa");
        btn_giaiMa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_giaiMa1ActionPerformed(evt);
            }
        });

        txt_DercyptedMess1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_DercyptedMess1ActionPerformed(evt);
            }
        });

        txt_EncryptedMess1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_EncryptedMess1ActionPerformed(evt);
            }
        });

        lbl_Encry.setText("Encrypted message:");

        lbl_Decry.setText("Decrypted message:");

        javax.swing.GroupLayout pnl_ChinhLayout = new javax.swing.GroupLayout(pnl_Chinh);
        pnl_Chinh.setLayout(pnl_ChinhLayout);
        pnl_ChinhLayout.setHorizontalGroup(
            pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ChinhLayout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ChinhLayout.createSequentialGroup()
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_tieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnl_ChinhLayout.createSequentialGroup()
                                .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_Plain, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_keyPublic, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_KeyPrivate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_Decry)
                                        .addComponent(lbl_Encry, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_KeyPrivate, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_KeyPublic, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_PlainText2, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_EncryptedMess1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_DercyptedMess1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(54, 54, 54)
                        .addComponent(btn_Res, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ChinhLayout.createSequentialGroup()
                        .addComponent(btn_giaiMa1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btn_MaHoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(211, 211, 211))))
        );
        pnl_ChinhLayout.setVerticalGroup(
            pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ChinhLayout.createSequentialGroup()
                .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_ChinhLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btn_Res, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243))
                    .addGroup(pnl_ChinhLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_tieuDe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_PlainText2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Plain, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_KeyPublic, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_keyPublic, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_KeyPrivate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_KeyPrivate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_EncryptedMess1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Encry, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_DercyptedMess1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Decry, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(pnl_ChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_giaiMa1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_MaHoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(pnl_Chinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_EncryptedMess1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_EncryptedMess1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_EncryptedMess1ActionPerformed

    private void txt_DercyptedMess1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DercyptedMess1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DercyptedMess1ActionPerformed

    private void btn_giaiMa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_giaiMa1ActionPerformed
        Encrypted();
    }//GEN-LAST:event_btn_giaiMa1ActionPerformed

    private void btn_MaHoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MaHoa1ActionPerformed
        Dercypted();
    }//GEN-LAST:event_btn_MaHoa1ActionPerformed

    private void btn_ResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResActionPerformed
        txt_DercyptedMess1.setText("");
        txt_EncryptedMess1.setText("");
        txt_KeyPrivate.setText("");
        txt_KeyPublic.setText("");
        txt_PlainText2.setText("");
    }//GEN-LAST:event_btn_ResActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Page_RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Page_RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Page_RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Page_RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Page_RSA().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_MaHoa1;
    private javax.swing.JButton btn_Res;
    private javax.swing.JButton btn_giaiMa1;
    private javax.swing.JLabel lbl_Decry;
    private javax.swing.JLabel lbl_Encry;
    private javax.swing.JLabel lbl_KeyPrivate;
    private javax.swing.JLabel lbl_Plain;
    private javax.swing.JLabel lbl_keyPublic;
    private javax.swing.JLabel lbl_tieuDe;
    private javax.swing.JPanel pnl_Chinh;
    private javax.swing.JTextField txt_DercyptedMess1;
    private javax.swing.JTextField txt_EncryptedMess1;
    private javax.swing.JTextField txt_KeyPrivate;
    private javax.swing.JTextField txt_KeyPublic;
    private javax.swing.JTextField txt_PlainText2;
    // End of variables declaration//GEN-END:variables
}
