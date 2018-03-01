/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageregistration;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author crl
 */
public class CommonMethod {
    
    public void displayImageFile(File f1, JLabel label, Size s) {
        Mat m1 = Imgcodecs.imread(f1.getAbsolutePath(), Imgcodecs.IMREAD_COLOR); 
        if (m1 != null) {
            displayMatImage(m1, label, s);
        }
    }

    public String extnOfSelImgFile(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            return fileName.substring(i + 1);
        }
        return "";
    }
    
    public String extnOfSelVidFile(String fileName) {
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            return fileName.substring(i + 1);
        }
        return "";
    }

    public String execShellCommand(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(CommonMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Mat displayMatImage(Mat m1, JLabel label, Size s) {
        Mat dst = new Mat();
        Imgproc.resize(m1, dst, s);

        BufferedImage bImage = Mat2BufferedImage(dst);
        ImageIcon image = new ImageIcon(bImage);
        label.setIcon(image);

        label.validate();
        label.repaint();
        return dst;
    }
    
    public BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage img = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return img;
    }
    
    public File[] chooseFile(ImageFusionInputWindow frame, String path) {

        final File dirToLock = new File(path);

        final JFileChooser file_chooser = new JFileChooser(dirToLock);
        file_chooser.setAcceptAllFileFilterUsed(false);
        file_chooser.setBounds(0, 0, 50, 50);

        file_chooser.setMultiSelectionEnabled(true);
        file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file_chooser.setVisible(true);
        file_chooser.setCurrentDirectory(dirToLock);
        int r = file_chooser.showOpenDialog(frame);
        if (r == JFileChooser.APPROVE_OPTION) {
            return file_chooser.getSelectedFiles();
        } else if (r == JFileChooser.CANCEL_OPTION) {
            return null;
        }

        return null;
    }
    
}
