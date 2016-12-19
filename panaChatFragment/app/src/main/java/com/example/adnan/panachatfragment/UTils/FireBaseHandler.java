package com.example.adnan.panachatfragment.UTils;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Adnan on 1/19/2016.
 */
public class FireBaseHandler {
    public static FireBaseHandler innstance;
    public static DatabaseReference fire;
    String key;

    public static String url = "https://saylanirrr.firebaseio.com/";

    public static FireBaseHandler getInstance() {
        if (innstance == null) {
            innstance = new FireBaseHandler();
        }

        return innstance;
    }

    public FireBaseHandler() {
//        fire = new Firebase(url);
//        fire.keepSynced(true);
        fire = FirebaseDatabase.getInstance().getReference();
    }

    public void getConversationListListener(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listner) {
fire.child("AppData").child("Friends").child(id).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
    @Override
    public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
        listner.sucess(dataSnapshot);
    }

    @Override
    public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

    }

    public void getFriendListListListener(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listner) {
        fire.child("AppData").child("Friends").child(id).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                listner.sucess(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getGroupListListener(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("MyGroups").child(id).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getFriendRequestListListener(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("FriendRequests").child(id).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
           listener.sucess(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        return key;
    }

    public void chat(String partnerId, String uid, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
//       Query query= ;
        fire.child("AppData").child("Conversations").child(uid).child(partnerId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
           listener.sucess(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getCurrentProfileData(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").child(id).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void searchBarAdaptorCheckFriend(String uid, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {

        fire.child("AppData").child("Friends").child(uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void friendListAdaptor(String senderId, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").child(senderId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void getUserProfileData(String uid, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").child(Global.uid).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void groupFragmentContextItemSelected(String uid, String groupName, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("MyGroups").child(uid).child(groupName).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void groupChatWork(String nameOfGroup, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("GroupData").child(nameOfGroup).child("Conversation").addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
           listener.sucess(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void friendProfile(String partnerId, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").child(partnerId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void FindFriendList(final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for(com.google.firebase.database.DataSnapshot data:dataSnapshot.getChildren()){
                    listener.sucess(data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void allGroupList(final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("GroupInfo").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for(com.google.firebase.database.DataSnapshot d:dataSnapshot.getChildren()){
                    listener.sucess(d);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void unFriend(final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {

        fire.child("AppData").child("Friends").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getCallerName(String id, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("BasicInfo").child(id).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteChatMsgs(String uid, String senderId, final InterFace<com.google.firebase.database.DataSnapshot, FirebaseError> listener) {
        fire.child("AppData").child("Conversations").child(uid).child(senderId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                listener.sucess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}