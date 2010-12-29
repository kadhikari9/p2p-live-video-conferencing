/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core;

import java.util.ArrayList;
import java.util.Iterator;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerAdvertisement;
import org.ioe.bct.p2pconference.dataobject.ProtectedPeerGroup;
import org.ioe.bct.p2pconference.ui.AppMainFrame;

/**
 *
 * @author kusum
 */
public class GroupDiscoveryThread extends Thread {
    PeerGroupOrganizer organizer;
    PeerGroupService peerGroupDiscoveryService;

    public GroupDiscoveryThread(PeerGroupOrganizer org){
        organizer=org;
        peerGroupDiscoveryService=new PeerGroupService();
    }

    @Override
    public void run()  {
          ArrayList<PeerGroup> newPeerGroup= peerGroupDiscoveryService.discoverGroups(AppMainFrame.netCOre.getNetPeerGroup());
          System.out.println("Total Groups: "+newPeerGroup.size());
         ArrayList<ProtectedPeerGroup> newprotectedGroups=new ArrayList<ProtectedPeerGroup>();

         Iterator<PeerGroup> it=newPeerGroup.iterator();
         while(it.hasNext()) {
             PeerGroup current=it.next();
             String peerGroupName=current.getPeerGroupName();
             ProtectedPeerGroup currentProtectedPeerGrp=new ProtectedPeerGroup(peerGroupName,"","",current);
             ArrayList<PeerAdvertisement> pperList=peerGroupDiscoveryService.discoverPeerInGroup(current);
             Iterator<PeerAdvertisement> peerListIterator=pperList.iterator();

             ArrayList nameList=new ArrayList();
             while(peerListIterator.hasNext()){
                 PeerAdvertisement adv=peerListIterator.next();
                 nameList.add(adv.getName());
             }
            currentProtectedPeerGrp.setConnectUsers(nameList);
            newprotectedGroups.add(currentProtectedPeerGrp);

            organizer.updateAllPeerGroups(newprotectedGroups);
         }
         try {
             Thread.sleep(30000);
         }
         catch (InterruptedException e){
             e.printStackTrace();
         }

    }

}
