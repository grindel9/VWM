import { Component, OnInit } from '@angular/core';
import { GridDataResult, DataStateChangeEvent} from '@progress/kendo-angular-grid';
import { PRODUCTS } from '../data/products';
import { State , process } from '@progress/kendo-data-query';

const distinctBrand = data => data
    .filter((x, idx, xs) => xs.findIndex(y => y.brand === x.brand) === idx);

const distinctMadeIn = data => data
    .filter((x, idx, xs)=> xs.findIndex(y => y.madeIn === x.madeIn) === idx);

@Component({
  selector: 'app-filterable-list',
  templateUrl: './filterable-list.component.html',
  styleUrls: ['./filterable-list.component.css']
})
export class FilterableListComponent implements OnInit {

  public state: State = {
    skip: 0,
    take: 20,
  };

  // filter properties

  public item:Filtermodel = {
    idMin: null,
    idMax: null,
    brand: null,
    madeIn: null,
    model: null,
    screenSizeMin: null,
    screenSizeMax: null,
    priceMin: null,
    priceMax: null,
    type: { t:null},
    status: { s:null}
  }

  public distinctBrand: any[] = distinctBrand(PRODUCTS);
  public distinctMadeIn: any[] = distinctMadeIn(PRODUCTS);

  public gridData: GridDataResult = process(PRODUCTS, this.state);

  constructor() { }

  ngOnInit(): void {
    console.log(distinctBrand(PRODUCTS));
  }

  public dataStateChange(state: DataStateChangeEvent): void {
    this.state = state;
    this.gridData = process(PRODUCTS, this.state);
  }

  addToCart() {
   window.alert('Your product has been added to the cart!');
  }

  clearFilter() {
    window.alert('Your product has been added to the cart!');
   }

  filter() {
    console.log(this.item);
  }
}

export interface IFiltermodel{
  idMin: number,
  idMax: number,
  brand: any,
  madeIn: any,
  model: string,
  screenSizeMin: number,
  screenSizeMax: number,
  priceMin: number,
  priceMax: number,
  type:string,
  status:string
}

export class Filtermodel implements IFiltermodel{
  idMin: number;
  idMax: number;
  brand: any;
  madeIn: any;
  model: string;
  screenSizeMin: number;
  screenSizeMax: number;
  priceMin: number;
  priceMax: number;
  type:any;
  status:any;
}