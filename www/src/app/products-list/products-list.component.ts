import { Component, OnInit } from '@angular/core';
import { ProductsService } from '../sevice/products-service';
import { Product } from '../model/product';

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.css']
})
export class ProductsListComponent implements OnInit {
  selected = "All";

  displayedColumns: string[] = ['brand', 'model', 'price', 'detail'];
  products: Product[];

  constructor(public service: ProductsService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.service.getProducts()
    .subscribe(products => this.products = products);
  }

  addToCart() {
    window.alert('Your product has been added to the cart!');
  }
}
