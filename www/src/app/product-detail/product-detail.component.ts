import { Component, OnInit} from '@angular/core';
import { Product } from '../model/product';
import { ActivatedRoute } from '@angular/router';
import { ProductsService } from '../sevice/products-service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  public count: number = 1;
  // data
  product: Product;

  constructor(
    private route: ActivatedRoute,
    private service: ProductsService
  ) { }

  ngOnInit(): void {
    this.getProduct();
  }

  getProduct(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.service.getProduct(id)
      .subscribe(product => this.product = product);
    console.log("neres to zatim, namockovana data");
    console.log(this.product);
  }

  addToCart() {
    window.alert('Your product has been added to the cart!');
  }

  increment() {
    this.count++;
  }

  decrement() {
    if(this.count>0)
      this.count--;
  }

}
