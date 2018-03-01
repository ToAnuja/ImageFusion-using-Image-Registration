/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageregistration;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import net.miginfocom.swing.MigLayout;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.INTER_LINEAR;

public class ImageFusionInputWindow extends JFrame implements ActionListener {

    /**
     *
     */
    public Cursor mouseCs = new Cursor(Cursor.CROSSHAIR_CURSOR);
    static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize().getSize();
    Size SIZE_IMAGE_FUSION = new Size((dimension.getWidth() / 2) - 10, dimension.getHeight() - 200);
    private final String path = "/home/crl/NetBeansProjects/OnlineStreamPlay";
    CommonMethod commObj = new CommonMethod();

    private static final long serialVersionUID = 1L;
    private static ImageFusionInputWindow ready_obj = new ImageFusionInputWindow();

    private final JPanel leftPanel;
    private final JPanel mainPanel;
    private final JPanel rightPanel;

    private final JButton btnDrawPoint1;
    private final JButton btnDrawPoint2;
    private final JButton btnImgFusion;
    private final JButton btnImgClose;
    private final JButton btnSelectImg1;
    private final JButton btnSelectImg2;
    private final JButton btnClearImg1;
    private final JButton btnClearImg2;

    private final JLabel lblLeft;
    private final JLabel lblright;
    
    private File file1, file2;
    private File tmpFile1, tmpFile2;

    private final Cursor defaultMouseCs;
    private String extn1;
    
    private Mat mat1, mat2;
    private Mat mat1_orig, mat2_orig;

    MatOfPoint2f srcPoints;
    MatOfPoint2f destPoints;

    ArrayList<org.opencv.core.Point> srcList = new ArrayList();
    ArrayList<org.opencv.core.Point> dstList = new ArrayList();

    public static ImageFusionInputWindow getInstance() {
        if (ready_obj == null) {
            ready_obj = new ImageFusionInputWindow();
        }
        return ready_obj;
    }

    ImageFusionInputWindow() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        srcPoints = new MatOfPoint2f();
        destPoints = new MatOfPoint2f();
      
        mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("inset 0, hidemode 2,nocache"));
        mainPanel.setBackground(Color.GRAY);

        leftPanel = new JPanel(new MigLayout("nocache, hidemode 3"));
        rightPanel = new JPanel(new MigLayout("nocache, hidemode 3"));

        btnDrawPoint1 = new JButton("Draw Point");
        btnDrawPoint1.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnDrawPoint1.setForeground(Color.darkGray);
        btnDrawPoint1.setBackground(Color.pink);
        btnDrawPoint1.addActionListener(this);
        btnDrawPoint1.setIcon(new javax.swing.ImageIcon("Icons/drawPoint.png"));

        btnImgClose = new JButton("Close");
        btnImgClose.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnImgClose.setForeground(Color.darkGray);
        btnImgClose.setBackground(Color.pink);
        btnImgClose.addActionListener(this);
        btnImgClose.setIcon(new javax.swing.ImageIcon("Icons/close.png"));

        btnDrawPoint2 = new JButton("Draw Point");
        btnDrawPoint2.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnDrawPoint2.setForeground(Color.darkGray);
        btnDrawPoint2.setBackground(Color.pink);
        btnDrawPoint2.addActionListener(this);
        btnDrawPoint2.setIcon(new javax.swing.ImageIcon("Icons/drawPoint.png"));

        btnClearImg1 = new JButton("Clear Points");
        btnClearImg1.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnClearImg1.setForeground(Color.darkGray);
        btnClearImg1.setBackground(Color.pink);
        btnClearImg1.addActionListener(this);
        btnClearImg1.setIcon(new javax.swing.ImageIcon("Icons/clear_new.png"));

        btnClearImg2 = new JButton("Clear Points");
        btnClearImg2.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnClearImg2.setForeground(Color.darkGray);
        btnClearImg2.setBackground(Color.pink);
        btnClearImg2.addActionListener(this);
        btnClearImg2.setIcon(new javax.swing.ImageIcon("Icons/clear_new.png"));

        btnImgFusion = new JButton("Fusion");
        btnImgFusion.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnImgFusion.setForeground(Color.darkGray);
        btnImgFusion.setBackground(Color.pink);
        btnImgFusion.addActionListener(this);
        btnImgFusion.setIcon(new javax.swing.ImageIcon("Icons/photos.png"));

        btnSelectImg1 = new JButton("Select Image");
        btnSelectImg1.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnSelectImg1.setForeground(Color.darkGray);
        btnSelectImg1.setBackground(Color.pink);
        btnSelectImg1.addActionListener(this);
        btnSelectImg1.setIcon(new javax.swing.ImageIcon("Icons/Select.png"));

        btnSelectImg2 = new JButton("Select Image");
        btnSelectImg2.setFont(new java.awt.Font("Cantarell", 1, 15));
        btnSelectImg2.setForeground(Color.darkGray);
        btnSelectImg2.setBackground(Color.pink);
        btnSelectImg2.addActionListener(this);
        btnSelectImg2.setIcon(new javax.swing.ImageIcon("Icons/Select.png"));

        lblLeft = new JLabel();
        lblright = new JLabel();

        leftPanel.add(btnSelectImg1,"gapleft 5%,width 30%, split 3");
        leftPanel.add(btnDrawPoint1, "gapleft 10%");
        leftPanel.add(btnClearImg1, "gapleft 1%, wrap");
        leftPanel.add(lblLeft, "width 100%,wrap");

        
        rightPanel.add(btnSelectImg2, "gapleft 5%,width 30%, split 3");
        rightPanel.add(btnDrawPoint2, "gapleft 10%");
        rightPanel.add(btnClearImg2, "gapleft 1%, wrap");
        rightPanel.add(lblright, "width 100%,wrap");

        mainPanel.add(leftPanel, "width 50%,height 97%,split 2");
        mainPanel.add(rightPanel, "width 50%,height 97%,wrap");
        mainPanel.add(btnImgFusion, "align center,width 10%,height 5%, split 2");
        mainPanel.add(btnImgClose, "align center,width 10%,height 5%,wrap");

        getContentPane().add(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Image Fusion");

        
        defaultMouseCs = btnImgFusion.getCursor();

        lblLeft.addMouseListener(mouseAdapter);
        lblright.addMouseListener(mouseAdapter);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClosingClear();
            }
        });
    }

    private void onClosingClear() {
        removeAll();
        file1 = null;
        file2 = null;
        dispose();
        setTitle("");
        ready_obj = null;
        btnDrawPoint1.setEnabled(true);
        btnDrawPoint2.setEnabled(true);
        if (tmpFile1 != null && tmpFile2 != null) {
            tmpFile1.delete();
            tmpFile2.delete();
        }
    }

    private MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jImgLabelMouseClicked(evt);
        }
    };


    private void jImgLabelMouseClicked(java.awt.event.MouseEvent evt) {

        if (evt.getClickCount() == 2) {
            JLabel label = (JLabel) evt.getSource();
            Point p = evt.getPoint();
            org.opencv.core.Point opencvPoint = new org.opencv.core.Point();
            opencvPoint.x = p.x;
            opencvPoint.y = p.y;

            if (label.equals(lblLeft) && !btnDrawPoint1.isEnabled()) {
                Imgproc.line(mat1, opencvPoint, opencvPoint, new Scalar(255, 0, 0), 15);
                commObj.displayMatImage(mat1, label, SIZE_IMAGE_FUSION);
                lblLeft.setCursor(defaultMouseCs);
                btnDrawPoint1.setEnabled(true);
                srcList.add(opencvPoint);

            } else if (label.equals(lblright) && !btnDrawPoint2.isEnabled()) {
                Imgproc.line(mat2, opencvPoint, opencvPoint, new Scalar(255, 0, 0), 15);
                commObj.displayMatImage(mat2, label, SIZE_IMAGE_FUSION);

                lblright.setCursor(defaultMouseCs);
                btnDrawPoint2.setEnabled(true);
                dstList.add(opencvPoint);

            }

        }
    }

    private void selectImageButton(boolean btton1Flg) {
        File[] fArray = commObj.chooseFile(this, path);

        if (fArray == null) {
            return;
        }
        if (fArray.length < 1) {
            JOptionPane.showMessageDialog(this, "SINGLE_FILE_SELECTION_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
            return;
        }
        if (fArray.length > 1) {
            JOptionPane.showMessageDialog(this, "MULTIPLE_FILE_SELECTION_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
            return;
        }

        if (btton1Flg) {

            file1 = fArray[0];
            extn1 = commObj.extnOfSelImgFile(file1.getName());
            mat1 = Imgcodecs.imread(fArray[0].getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
            mat1 = commObj.displayMatImage(mat1, lblLeft, SIZE_IMAGE_FUSION);
            mat1_orig = new Mat();
            mat1_orig = mat1.clone();
        } else {
            file2 = fArray[0];
            mat2 = Imgcodecs.imread(fArray[0].getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
            mat2 = commObj.displayMatImage(mat2, lblright, SIZE_IMAGE_FUSION);
            mat2_orig = new Mat();
            mat2_orig = mat2.clone();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnSelectImg1)) {
            selectImageButton(true);
        } else if (e.getSource().equals(btnSelectImg2)) {
            selectImageButton(false);
        } else if (e.getSource().equals(btnDrawPoint1)) {
            if (file1 != null) {
                lblLeft.setCursor(mouseCs);
                btnDrawPoint1.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "SINGLE_FILE_SELECTION_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
            }
        } else if (e.getSource().equals(btnDrawPoint2)) {
            if (file2 != null) {
                lblright.setCursor(mouseCs);
                btnDrawPoint2.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "SINGLE_FILE_SELECTION_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
            }
        } else if (e.getSource().equals(btnImgFusion)) {
            if (file1 != null && file2 != null) {
                imageFusionResultShow();
            } else {
                JOptionPane.showMessageDialog(this, "SINGLE_FILE_SELECTION_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
            }
        } else if (e.getSource().equals(btnClearImg1)) {
            clearPointsFromImage(true);
        } else if (e.getSource().equals(btnClearImg2)) {
            clearPointsFromImage(false);
        }

    }

    private void clearPointsFromImage(boolean button1Flg) {
        if (button1Flg) {
            commObj.displayMatImage(mat1_orig, lblLeft, SIZE_IMAGE_FUSION);
        } else {
            commObj.displayMatImage(mat2_orig, lblright, SIZE_IMAGE_FUSION);
        }
        srcList.clear();
        dstList.clear();
    }

    private void imageFusionResultShow() {
        try {
            double alpha = 0.5;
            double beta = 1.0 - alpha;
            srcPoints.fromList(srcList);
            destPoints.fromList(dstList);
            Mat H = Calib3d.findHomography(srcPoints, destPoints);
            if (H.empty()) {
                System.out.println("H is empty");
                System.exit(0);
            }
            Mat warpMat = new Mat();
            Imgproc.warpPerspective(mat2_orig, warpMat, H.inv(), SIZE_IMAGE_FUSION, INTER_LINEAR);
            Mat sobel = new Mat();
            Imgproc.Sobel(warpMat, sobel, -1, 1, 1);

            Mat dst = new Mat();
            Core.addWeighted(mat1_orig, alpha, sobel, beta, 0.0, dst);

            String fusionName = new StringBuilder("fusionResult.").append(extn1).toString();
            File ouptutFile = new File(fusionName);
            BufferedImage fuseImageBuff = commObj.Mat2BufferedImage(dst);
            ImageIO.write(fuseImageBuff, extn1, ouptutFile);
            String cmd = new StringBuilder("xdg-open ").append(fusionName).toString();
            commObj.execShellCommand(cmd);

        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(this, "IMAGE_FUSION_ERROR_MESSAGE", "ERROR", JOptionPane.ERROR_MESSAGE, new ImageIcon("Icons/error.png"));
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImageFusionInputWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImageFusionInputWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImageFusionInputWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImageFusionInputWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageFusionInputWindow img = new ImageFusionInputWindow();
                img.setSize(dimension);
                img.setVisible(true);
            }
        });
    }

}
