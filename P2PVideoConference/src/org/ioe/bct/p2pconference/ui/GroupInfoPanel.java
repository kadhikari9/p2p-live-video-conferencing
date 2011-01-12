/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GroupInfoPanel.java
 *
 * Created on Dec 25, 2010, 6:23:16 PM
 */
package org.ioe.bct.p2pconference.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.core.AudioConference;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.patterns.mediator.Mediator;

/**
 *
 * @author kusu
 */
public class GroupInfoPanel extends javax.swing.JPanel {

    private ProtectedPeerGroup peerGroup;
    private Mediator confMediator;
    /** Creates new form GroupInfoPanel */
    public GroupInfoPanel(ProtectedPeerGroup peerGroup) {
        initComponents();
        this.peerGroup = peerGroup;


        updateComponents();
    }

    public final synchronized void updateComponents() {
        int users = peerGroup.getConnectedUsers().size();
        System.out.println(peerGroup.getGroupName() + " Total connected: " + users);
        if (users < 2) {
            groupContactsPanel.removeAll();
            startButton.setEnabled(false);
            validate();

        }

        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 5, 5);
        groupContactsPanel.setLayout(layout);
        for (int i = 0; i < users; i++) {

            PeerAdvertisement adv = peerGroup.getConnectedUsers().get(i);
            String peerName = adv.getName();

            JPanel newPanel = new JPanel();
            newPanel.setPreferredSize(new Dimension(125, 25));
            newPanel.setBorder(BorderFactory.createLineBorder(Color.cyan));
            newPanel.add(new JLabel(peerName));
            newPanel.validate();
            groupContactsPanel.add(newPanel);

        }

        groupContactsPanel.validate();
    }
    public void setMediator(Mediator med)
    {
        confMediator=med;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupContactsPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        groupContactsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout groupContactsPanelLayout = new javax.swing.GroupLayout(groupContactsPanel);
        groupContactsPanel.setLayout(groupContactsPanelLayout);
        groupContactsPanelLayout.setHorizontalGroup(
            groupContactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
        );
        groupContactsPanelLayout.setVerticalGroup(
            groupContactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );

        add(groupContactsPanel, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(150, 147));

        startButton.setText("Start Conference");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(startButton)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        // TODO add your handling code here:
        AudioConference audioConference=new AudioConference(peerGroup.getPeerGroup(),AppMainFrame.getUserName(),confMediator);
        final Thread publishThread=new Thread(audioConference.new PublishModuleSpecHandler());
        final Thread discoveryThread=new Thread(audioConference.new DiscoveryAdvertisementHandler());
        final Thread receiveThread=new Thread(audioConference.new ReceiveMessageHandler(peerGroup));
        receiveThread.setPriority(Thread.MAX_PRIORITY);
        final Thread captureThread=new Thread(audioConference.new AudioCaptureBeginThread());
        final Thread sendThread=new Thread (audioConference.new SendMessageHandler());

        class AudioConferenceHandler implements Runnable{
            public void run()
            {
                publishThread.start();
                discoveryThread.start();
                receiveThread.start();
                captureThread.start();
                sendThread.start();

            }
        }

        Thread audioConferenceThread=new Thread(new AudioConferenceHandler());
        audioConferenceThread.start();


    }//GEN-LAST:event_startButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel groupContactsPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
