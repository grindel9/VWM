import { Component, OnInit } from '@angular/core';
import { ProductsService } from '../sevice/products-service';
import { PRODUCTS } from '../data/products';
import { process, State } from '@progress/kendo-data-query';
import { DataStateChangeEvent, GridDataResult } from '@progress/kendo-angular-grid';

const distinctType = data => data
    .filter((x, idx, xs) => xs.findIndex(y => y.type === x.type) === idx);

const distinctMadeIn = data => data
.filter((x, idx, xs) => xs.findIndex(y => y.madeIn === x.madeIn) === idx);

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.css']
})
export class ProductsListComponent {
  
  public state: State = {
    skip: 0,
    take: 5,
};

public gridData: GridDataResult = process(PRODUCTS, this.state);
public distinctType: any[] = distinctType(PRODUCTS);
public distinctMadeIn: any[] = distinctMadeIn(PRODUCTS);

public dataStateChange(state: DataStateChangeEvent): void {
    this.state = state;
    this.gridData = process(PRODUCTS, this.state);
}

  // getProducts(): void {
  //   this.service.getProducts()
  //   .subscribe(products => this.products = products);
  // }

  addToCart() {
    window.alert('Your product has been added to the cart!');
  }
}
