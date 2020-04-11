package com.sihhackathon.e_omfed.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.ConfirmOrderAtivity;
import com.sihhackathon.e_omfed.Model.Cart;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ViewHolder.CartViewHolder;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.sihhackathon.e_omfed.ViewProductDetails;
import com.sihhackathon.e_omfed.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    View CartView;

    private RecyclerView recyclerView;
    private TextView total_price;
    private Button next_btn;
    Fragment fragment=new HomeFragment();
    private int Total_price_cart=0 ,oneTypePrice=0;

    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        CartView = inflater.inflate(R.layout.fragment_slideshow, container, false);

        next_btn=(Button)CartView.findViewById(R.id.next_process_btn);
        total_price=(TextView) CartView.findViewById(R.id.total_price_txt);

        recyclerView=(RecyclerView)CartView.findViewById(R.id.recycler_cart_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList=new ArrayList<Products>();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_price.setText("Total Price = ₹ "+(String.valueOf(Total_price_cart)));

                startActivity(new Intent(getActivity(), ConfirmOrderAtivity.class).putExtra("Total Price",(String.valueOf(Total_price_cart))));

            }
        });






        return CartView;
    }


    @Override
    public void onStart() {
        super.onStart();




        final DatabaseReference cartlistREf= FirebaseDatabase.getInstance().getReference().child("Cart_List");
        cartlistREf.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartlistREf.child("user_view")
                .child(Prevalent.currentOnlineUsers.getPhone_number()).child("PRODUCTS"), Cart.class).build();

       adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {


               cartViewHolder.txtproduct_name.setText(cart.getPname());
               cartViewHolder.txtproduct_quantity.setText("Quantity= "+cart.getQuantity());
               cartViewHolder.txtproduct_price.setText("Price :₹"+cart.getPrice());

               oneTypePrice = (Integer.valueOf(cart.getPrice()) * (Integer.valueOf(cart.getQuantity())));

               Total_price_cart=Total_price_cart+oneTypePrice;


               cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       CharSequence options[] =new CharSequence[]
                               {
                                       "Edit",
                                       "Remove"
                               };

                       AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                       builder.setTitle("Cart Options");

                       builder.setItems(options, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               if(which==0)
                               {
                                   total_price.setText("Total Price=");
                                   Total_price_cart=0;
                                   Intent i = new Intent(getActivity(), ViewProductDetails.class).putExtra("pid", cart.getPid());
                                   startActivity(i);

                               }
                               if (which==1)
                               {
                                   cartlistREf.child("user_view").child(Prevalent.currentOnlineUsers.getPhone_number()).child("PRODUCTS")
                                           .child(cart.getPid()).removeValue()
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                                   total_price.setText("Total Price=");
                                                   Total_price_cart=0;

                                                   Toast.makeText(getContext(), cart.getPname()+" item removed", Toast.LENGTH_SHORT).show();


//                                                   getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();


                                               }
                                           });

                               }

                           }
                       });

                       builder.show();
                   }
               });



           }

           @NonNull
           @Override
           public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               return new CartViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.cart_items_layout,parent,false));

           }
       };

        recyclerView.setAdapter(adapter);

        adapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}