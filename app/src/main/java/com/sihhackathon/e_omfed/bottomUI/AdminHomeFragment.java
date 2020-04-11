package com.sihhackathon.e_omfed.bottomUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.AdminActivity;
import com.sihhackathon.e_omfed.AdminHomeActivity;
import com.sihhackathon.e_omfed.AdminManageProducts;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.sihhackathon.e_omfed.ViewProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment
{

    private RecyclerView recyclerView;
    private EditText search_text;
    private ImageButton search_btn;
    private String searchInput;

    private DatabaseReference ProductRef;
    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Products> options;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View homeView= inflater.inflate(R.layout.fragment_bottom_home, container,false);

        search_btn=(ImageButton)homeView.findViewById(R.id.search_btn1);
        search_text=(EditText)homeView.findViewById(R.id.search_text_admin);


        recyclerView=(RecyclerView)homeView.findViewById(R.id.recycler_admin_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList=new ArrayList<Products>();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchInput=search_text.getText().toString();

                onStart();
            }
        });






        return homeView;

    }

    @Override
    public void onStart() {
        super.onStart();

        ProductRef= FirebaseDatabase.getInstance().getReference().child("products");
        ProductRef.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductRef.orderByChild("Product_name").startAt(searchInput) ,Products.class).build();


        adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {

                productViewHolder.Productname.setText(products.getProduct_name());
                productViewHolder.Productdescription.setText(products.getProduct_Description());
                productViewHolder.Productprice.setText("₹ " +products.getProduct_price());

                Picasso.get().load(products.getImage_URL()).into(productViewHolder.product_image);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] =new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("Manage Products");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {
                                    startActivity(new Intent(getActivity(), AdminManageProducts.class).putExtra("pid", products.getPid()));



                                }
                                if (which==1)
                                {

                                    ProductRef.child(products.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(getContext(), "product removed", Toast.LENGTH_SHORT).show();

                                            }
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
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_layout,parent,false));
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
